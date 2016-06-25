package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeadException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 秒杀业务-控制器
 * @author Yuwl on 2016年6月16日
 */
@Controller
@RequestMapping("seckill")// url:模块/资源/{id}/细分  /seckill/list
public class SeckillController {
	
	private Logger logger = LoggerFactory.getLogger(SeckillController.class);
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model){
		List<Seckill> list = seckillService.queryAll();
		model.addAttribute("list", list);//model
		return "list";//view
	}
	
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId")Long seckillId,Model model){
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.queryById(seckillId);
		if(seckill == null){
			return "redirect:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	//ajax json
	@RequestMapping(value="/{seckillId}/exposer",
			method=RequestMethod.POST,
			produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId){
		SeckillResult<Exposer> result = null;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = new SeckillResult<>(false, e.getMessage());
		}
		return result;
	}
	
	//json ajax
	@RequestMapping(value="/{seckillId}/{md5}/execute",
					method=RequestMethod.POST,
					produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
					@PathVariable("md5")String md5,
					@CookieValue(value="killPhone",required=false)Long phone){
		SeckillResult<SeckillExecution> result = null;
		if(phone == null){
			result = new SeckillResult<>(false, "手机号未注册");
		}
		try {
			//使用Spring事务
//			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
			//使用存储过程
			SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(RepeadException e){
			logger.error(e.getMessage());
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(SeckillCloseException e){
			logger.error(e.getMessage());
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (Exception e) {
			logger.error(e.getMessage());
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INSERT_ERROR);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		}
		return result;
	}
	
	//json ajax
	@RequestMapping(value="/time/now",
					method=RequestMethod.GET,
					produces={"application/json;UTF-8"})
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}

}

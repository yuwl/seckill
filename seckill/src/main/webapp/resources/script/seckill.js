/**
 * 存在交互逻辑的js代码
 * javascript 模块化
 * seckill.detail.init()
 */
var seckill = {
	//封装秒杀相关的ajax的url
	URL : {
		now : "/seckill/seckill/time/now",
		exposer : function(seckillId){
			return "/seckill/seckill/" + seckillId + "/exposer";
		},
		execute : function(seckillId,md5){
			return '/seckill/seckill/' + seckillId + '/' + md5 + '/execute';
		}
	},
	validatePhone : function(phone){
		if(phone && phone.length == 11 && !isNaN(phone)){
			return true;
		}else{
			return false;
		}
	},
	countDown : function(seckillId,nowTime,startTime,endTime){
		var seckillBox = $("#seckill-box");
		if(nowTime > endTime){
			seckillBox.html("秒杀结束");
		}else if(nowTime < startTime){//秒杀未开始，倒计时
			var killTime = new Date(parseFloat(startTime)+1000);
			seckillBox.countdown(killTime,function(event){
				var format = event.strftime("秒杀倒计时：%D天 %H时 %M分 %S秒");
				seckillBox.html(format);
			}).on("finish_countdown",function(){
				//获取秒杀地址，控制实现逻辑，秒杀开始
				seckill.handleSeckillKill(seckillId, seckillBox);
			});
		}else{//秒杀开始
			seckill.handleSeckillKill(seckillId, seckillBox);
		}
	},
	handleSeckillKill : function(seckillId,node){
		//获取秒杀地址，控制实现逻辑，秒杀开始
		node.hide()
			.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//按钮
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			if(result && result['success']){
				var exposer = result['data'];
				if(exposer['exposed']){//开始秒杀
					var md5 = exposer['md5'];
					var killUrl = seckill.URL.execute(seckillId, md5);
					$("#killBtn").one("click",function(){
						//执行秒杀请求
						$(this).addClass("disabled");
						$.post(killUrl,{},function(result){
							if(result && result['success']){
								var killResult = result['data'];
								var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];
								node.html('<span class="label label-success">'+stateInfo+'<span>');
							}else{
								console.log("post execute :"+result);
							}
						});
					});
					node.show();
				}else{//秒杀未开始
					var now = exposer['now'];
					var start = exposer['start'];
					var end = exposer['end'];
					//重新计算计时逻辑
					seckill.countDown(seckillId, now, start, end);
				}
			}else{
				console.log("post exposer: "+result);
			}
		});
	},
	//秒杀页逻辑
	detail : {
		//详情页初始化
		init : function(params){
			//手机验证和登录，倒计时交互
			//在cookie中查找手机号
			var killPhone = $.cookie("killPhone");
			//验证手机号
			if(!seckill.validatePhone(killPhone)){
				//绑定phone
				var killPhoneModal = $("#killPhoneModal");
				killPhoneModal.modal({
					show : true,//显示弹出层，fade隐藏
					backdrop : "static",//禁止位置关闭
					keyboard : false //关闭键盘事件，如Esc退出
				});
				$("#killPhoneBtn").click(function(){
					var inputPhone = $("#killPhoneKey").val();
					if(seckill.validatePhone(inputPhone)){
						//电话写入cookie
						$.cookie("killPhone",inputPhone,{expires:7,path:"/seckill"});
						window.location.reload();
					}else{
						$("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误</label>').show(300);
					}
				});
			}
			//已经登录，倒计时交互
			var seckillId = params['seckillId'];
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			$.get(seckill.URL.now,{},function(result){
				if(result && result['success']){
					var nowTime = result['data'];
					seckill.countDown(seckillId, nowTime, startTime, endTime);
				}else{
					console.log("now error: "+result);
				}
			});
		}
		
	}	
		
};
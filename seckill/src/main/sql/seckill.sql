-- 秒杀执行存储过程
DELIMITER $$ -- 将 结束符 ; 转换为 $$
-- 定义存储过程
-- 参数：in 输入参数		out 输出参数
-- row_count()返回上一条修改（insert,update,delete）类型sql的修改行数
-- row_count():	0:未修改数据；>0:表示修改的行数；<0:表示出错了
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id bigint, in v_phone bigint,
 in v_kill_time timestamp,out r_result int)
BEGIN
	DECLARE insert_count int default 0;
	START TRANSACTION;
	insert ignore into success_killed(seckill_id,user_phone,create_time)
	values(v_seckill_id,v_phone,v_kill_time);
	select row_count() into insert_count;
	IF(insert_count = 0) THEN
		rollback;
		set r_result = -1; -- 重复秒杀返回0
	ELSEIF(insert_count < 0) THEN
		rollback;
		set r_result = -2; -- 出错了
	ELSE
		update seckill set number = number - 1
		where seckill_id = v_seckill_id
			and end_time > v_kill_time
			and start_time < v_kill_time
			and number > 0;
		select row_count() into insert_count;
		IF(insert_count = 0) THEN
			rollback;
			set r_result = 0; 
		ELSEIF(insert_count < 0) THEN
			rollback;
			set r_result = -2;
		ELSE
			commit;
			set r_result = 1;
		END IF;
	END IF;	
END
$$ -- 存储过程定义结束

DELIMITER ; -- 将 结束符 $$ 转换为 ;
-- 调用存储过程
set @r_result = -3; --定义变量@
-- 执行存储过程
call `seckill`.execute_seckill(1000,13520098518,now(),@r_result);
-- 获取结果
select @r_result;

-- 存储过程
-- 1.存储过程 优化事务行级锁持有的时间
-- 2.不要过度使用存储过程
-- 3.将插入，更新放入存储过程执行,替换Java事务
-- 4.银行用得多，互联网公司用的少
-- 5.QPS：一个商品6000/QPS

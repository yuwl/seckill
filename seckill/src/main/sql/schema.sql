-- 数据库初始化脚本
-- 创建数据库
create database seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
create table seckill(
	`seckill_id` bigint not null auto_increment comment '商品库存id',
	`name` varchar(120) not null comment '商品名称',
	`number` int not null comment '库存数量',
	`start_time` timestamp not null comment '秒杀开始时间',
	`end_time` timestamp not null comment '秒杀结束时间',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	primary key(seckill_id),
	key idx_start_time(start_time),
	key idx_end_time(end_time),
	key idx_create_time(create_time)
)engine=InnoDB Auto_Increment=1000 default charset=utf8 comment='秒杀库存表';
-- 初始化数据
insert into 
	seckill(name,number,start_time,end_time)
values
	('1000元秒杀iphone6',100,'2016-07-01 00:00:00','2016-07-02 00:00:00'),
	('500元秒杀ipad2',200,'2016-07-01 00:00:00','2016-07-02 00:00:00'),
	('300元秒杀小米4',300,'2016-07-01 00:00:00','2016-07-02 00:00:00'),
	('200元秒杀红米note',400,'2016-07-01 00:00:00','2016-07-02 00:00:00');
	
-- 秒杀成功明细表
create table success_killed(
	`seckill_id` bigint not null comment '商品库存id',
	`user_phone` bigint not null comment '用户手机号',
	`state` tinyint not null default -1 comment '状态标识-1：无效 0：成功',
	`create_time` timestamp not null comment '创建时间',
	primary key(seckill_id,user_phone),/**联合主键**/
	key idx_create_time(create_time)
)engine=InnoDB charset=utf8 comment='秒杀成功明细表';

-- 连接数据库控制台
mysql -uroot -p123456

-- 查看有哪些表
show tables;

-- 查看表创建的ddl语句
show create table seckill\G;

-- 为什么手写DDL
-- 记录每次上线的DDL修改
-- 上线V1.1
alter table seckill
drop index idx_create_time,
add index idx_c_s(start_time,create_time);
-- 上线V1.2
-- 
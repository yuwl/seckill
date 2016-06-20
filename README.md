# seckill

《Java高并发秒杀API》是慕课网系列课程，共四门课，分别为业务分析和DAO层，Service层，Web层和高并发优化。

此为自己实践的手敲源码，以下为总结：

DAO层：
1.数据库表DDL编写&SQl语句实现
	创建索引
	联合主键（防止数据重复）
	ignore消除主键冲突报错（影响行数为0）：
	insert ignore into table 
	多对一查询属性映射
2.采用Mybatis + Spring
3.Mybatis采用mapper接口动态实现
	配置技巧有：
	自动扫描Entity，使用别名
	自动扫描mapper配置文件，不用手动添加
	开启列名驼峰命名转换
	使用列别名替换列名
	允许 JDBC 支持自动生成主键
	方法参数-映射器注解@Param
4.Junit4 + Spring整合测试


Service层
1.接口设计考虑的方面
	方法定义粒度、参数、返回类型
2.使用DTO，Service、Web层的数据传输
3.自定义运行时异常
4.log日志：slf4j + logback实现
	将错误日志添加到logger中
5.Spring基于注解的声明式事务
	语法：@Transactional
	优点：
	相比xml配置方式，不是所有的方法都需要事务，定点添加
	要求：
	1).开发团队达成一致，明确注解式事务的编程风格
	2).保证事务方法执行的时间尽可能短，不要穿插网络操作，如Http/Rpc/缓存等
		剥离到事务方法外部，做一个更上层的方法，获取到数据后再进到事务处理
	3).不是每个方法都需要加事务
	事务回滚：
	默认Spring事务只在发生未被捕获的RuntimeExcetpion时才回滚
	所以，只有抛出运行期异常，Spring事务才会回滚
	不要catch完了不处理，或打印出来e.printStackTrace()这样做毫无用处，不会回滚。
	
Web层：
1.采用SpringMVC实现
2.采用Restful接口实现
	url:模块/资源/{id}/细分 
	Get /seckill/list
	Get /seckill/{seckillId}/detail
	Post /seckill/{seckillId}/exposer
	Post /seckill/{seckillId}/{md5}/execute
3.使用枚举来实现数据字典常量
4.模块化开发javascript代码
	seckill.detail.init()
5.基于Bootstrap开发页面

  
  

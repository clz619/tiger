# tiger说明

#### 如果阅读完文档后，还有任何疑问，请mail to tengkai.yuan@dianping.com

**tiger**是一种分布式事件调度框架，用于解决触发式延时任务的业务场景，偏重于执行层面，同一种任务可以由多台机器同时执行，并能保证一条任务不被重复执行。

tiger主要有以下三块组成：

1. zk集群管理：用于管理应用机器的在线情况，进而对机器可执行的任务节点进行自适应分配，保证一个任务同一时间只会被一台机器消费;

2. 事件调度管理：用于每隔一定时间触发一次任务执行，并监听任务执行器的配置情况，一旦发生变化，即停止任务执行，重新设置后再触发任务执行;

3. 任务执行管理：用于管理本机所分配到的执行器节点,进而进行任务节点捞取、任务过滤等,并对任务的执行结果进行处理;

#### 业务应用场景举例
http://www.12306.cn 上购买火车票的例子：

用户a在12306上提交订单后，会提示请在45分钟内支付，不然就会取消订单。

这样的情形很适合tiger来解决，步骤：

1)  插入一条[订单取消任务]，并设置执行时间45分钟后，addDispatchTask(arg0)

2)  实现任务分发接口DispatchHandler,实现订单取消的业务逻辑（做订单是否已支付的判断）

45分钟后，tiger会自动触发[订单取消任务]。

业务代码逻辑判断：如果此时订单已支付，那么返回任务丢弃；如果订单没支付，那么执行订单取消逻辑，成功后返回。

###### 总结：tiger适合任何触发式延时任务的业务场景.

### Documentation
**单体式部署请看：**
[UserGuide_1](https://github.com/tkyuan/tiger/blob/master/UserGuide_1.md)

**微服务部署请看：**
[UserGuide_2](https://github.com/tkyuan/tiger/blob/master/UserGuide_2.md)

### Comitters
#### 袁腾凯  tengkai.yuan@dianping.com
#### 许雪里  xueli.xue@dianping.com


**Thanks**

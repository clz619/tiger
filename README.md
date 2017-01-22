# tiger说明

#### 如果阅读完文档后，还有任何疑问，请mail to tengkai.yuan@dianping.com

**tiger**是一种分布式事件调度框架，用于解决触发式延时任务的业务场景，偏重于执行层面，同一种任务可以由多台机器同时执行，并能保证一条任务不被重复执行。

tiger主要有以下三块组成：

1. zk集群管理：用于管理应用机器的在线情况，进而对机器可执行的任务节点进行自适应分配，保证一个任务同一时间只会被一台机器消费;

2. 事件调度管理：用于每隔一定时间触发一次任务执行，并监听任务执行器的配置情况，一旦发生变化，即停止任务执行，重新设置后再触发任务执行;

3. 任务执行管理：用于管理本机所分配到的执行器节点,进而进行任务节点捞取、任务过滤等,并对任务的执行结果进行处理;

#### 业务应用场景举例
http://www.12306.cn 上购买火车票的例子：

用户a在12306上提交订单后，会提示请在30分钟内支付，不然就会取消订单。

这样的情形很适合tiger来解决，步骤：

1)  插入一条[订单取消任务]，并设置执行时间30分钟后，addDispatchTask(arg0)

2)  实现任务分发接口DispatchHandler,实现订单取消的业务逻辑（做订单是否已支付的判断）

30分钟后，tiger会自动触发[订单取消任务]。

业务代码逻辑判断：如果此时订单已支付，那么返回任务丢弃；如果订单没支付，那么执行订单取消逻辑，成功后返回。

###### 总结：tiger适合任何触发式延时任务的业务场景.

### 一、Tiger总体架构
![infra](http://code.dianpingoa.com/shop-business/wed-tiger/raw/master/tiger-service/src/main/resources/img/tigerinfra.png)

### 二、术语说明
##### 1. 分配策略divideType
```
假设线上2台机器，虚拟节点数设置为10，那么以下两种模式的分配结果：
1.1 散列模式：一台机器负责虚拟节点0,2,4,6,8;另一台机器负责虚拟节点1,3,5,7,9;

1.2 分块模式：一台机器负责虚拟节点0,1,2,3,4;另一台机器负责虚拟节点5,6,7,8,9;
```

#### 2. 执行器策略taskStrategy
```
2.1 统一捞取任务策略：有一个tiger默认的handler来捞取各个业务handler，然后再分发执行，不足之处是当某个handler1的任务特别多时，其它的handler2任务可能会长时间捞取不到；

2.2 各个执行器各自捞取任务策略：各个业务handler捞取各自所对应的任务，handler与handler之间完全独立；不足之处是，每一个handler都会对应自己的一个线程池，handler种类多时，比较耗线程资源，同时会增大一定的db压力；
```
#### 3. 巡航模式enableNavigate
巡航模式主要用于解决当任务不是很多时，tiger会通过算法自动分析某次任务轮询是否需要执行，以减少每次轮询任务的db压力；

#### 4. 反压模式enableBackFetch
反压模式主要用于解决当任务很多时，为了减少任务积压，只要该次轮询的任务数量达到了捞取上限（比如200个），就会自动再捞取一次（100个），相当于一次轮询会捞取两次任务（总共300个）；

#### 5. 任务组handlerGroup
任务组主要为了区分各个业务线，不同的业务线最好用不同的名称，保证全局唯一.

### 三、Documentation
**单体式部署请看：**
[UserGuide_1](http://code.dianpingoa.com/shop-business/wed-tiger/blob/master/UserGuide_1.md)

**微服务部署请看（对业务应用方接入只需看其中的3.1～3.7步骤）：**
[UserGuide_2](http://code.dianpingoa.com/shop-business/wed-tiger/blob/master/UserGuide_2.md)

**监控部署请看（对业务应用方接入只需看其中的第三个步骤）：**
[MonitorGuide](http://code.dianpingoa.com/shop-business/wed-tiger/blob/master/MonitorGuide.md)



### 四、Comitters
#### 袁腾凯  tengkai.yuan@dianping.com
#### 许雪里  xueli.xue@dianping.com


**Thanks**

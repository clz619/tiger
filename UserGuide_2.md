# tiger说明-微服务部署方式

微服务部署的jar依赖如下图：

![weifuwu](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/weifuwu.png)

其中，

**tiger-engine**是核心组件；

**tiger-core**提供addDispatchTask接口的client实现，其内部需要通过rpc方式远程调用tiger-biz的服务端实现；

**tiger-biz**主要用来与任务储存打交道，tiger-api的服务端实现，需要暴露一个rpc接口；

**tiger-service**是war包，进行服务部署；

**appServer** 是应用方war包，这里实现业务的handler。

## ======Quick Start======
### Step一. 环境配置
本项目在如下环境下运行良好

1）容器：tomcat 6.0及以上

2）zookeeper服务：zookeeper-3.3.6版本

3）mysql: mysql-5.5.49版本


### Step二. 个性化更改
#### ======tiger-service(war)======

1）数据源：配置在tiger-biz里的spring-db-config.xml，可替换c3p0DataSource；

2）orm层：项目用的是ibatis，如有不同，可改用其它orm框架;

3）任务表结构：在tiger-biz里的tables_mysql_tiger.sql，建议做适当的索引；

#### ======appServer(war)======
1）日志输出：log4j.xml可以根据自己的需求定义，但请保证tiger框架日志(appender name="TIGER")的输出。

```
<appender name="TIGER" class="org.apache.log4j.DailyRollingFileAppender">
        <param name="file" value="/data/applogs/tiger-service/logs/tiger.log"/>
        <param name="append" value="true"/>
        <param name="encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-d{yyyy-MM-dd HH:mm:ss} [%c]-[%t]-[%M]-[%L]-[%p] %m%n"/>
        </layout>
    </appender>
    
<logger name="com.dianping.tiger.engine" additivity="false">
      <level value="INFO"/>
      <appender-ref ref="TIGER"/>
</logger>
```
注：TIGER appender的日志输出级别可以根据需要调整为WARN级别。

### Step三. 项目启动
#### ======tiger-service(war)====
1) 在tiger-biz里暴露出以下接口实现的 **rpc服务**：

```
com.dianping.tiger.biz.task.service.impl.DispatchTaskMultiServiceImpl

com.dianping.tiger.biz.task.service.impl.DispatchTaskSingleServiceImpl
```

2) 在tiger-core中的spring-tiger.xml配置成 **rpc调用方式**（如下为本地直接调用方式），bean id保持不变：

```
	<bean id="dispatchTaskMultiBizService" class="com.dianping.tiger.biz.task.service.impl.DispatchTaskMultiServiceImpl"/>
	<bean id="dispatchTaskSingleBizService" class="com.dianping.tiger.biz.task.service.impl.DispatchTaskSingleServiceImpl"/>

```

启动zookeeper服务，启动tiger-service即可。

#### ======appServer(war)====
#### 3.1 依赖tiger-core
```
<dependency>
    <groupId>com.dianping</groupId>
    <artifactId>tiger-core</artifactId>
    <version>2.1.2</version>
</dependency>
```

#### 3.2 配置tigerConfigManager
在spring xml中配置tigerConfigManager

```
<bean id="tigerConfigManager" class="com.dianping.tiger.core.TigerConfigManager">
		<!-- 必须,设置轮询任务间隔,20s一次 -->
		<property name="interval" value="20"/>
		<!-- 必须,设置zk集群地址,请使用${wedding-smsplatform-server.zkaddress} -->
		<property name="zkConnectAddress" value="127.0.0.1:2181,10.25.13.11:2181"/>
		<!-- 必须,设置任务组名，要保证唯一，最好使用应用名称 -->
		<property name="handlerGroup" value="weddingTiger"/>
		<!-- 必须,设置需要执行的任务,支持动态调整 -->
		<property name="handlers" value="demoHandler1,demoHandler2"/>
		<!-- 设置虚拟节点个数，个数需至少大于集群机器数,默认100 -->
		<property name="virtualNodeNum" value="50"/>
		<!-- 设置zk虚拟节点分配策略,0-散列模式,1－分块模式,默认分块模式 -->
		<property name="divideType" value="1"/>
		<!-- 设置执行器策略，0-统一捞取任务策略；1-各个执行器各自捞取任务策略，默认为1 -->
		<property name="taskStrategy" value="1"/>
		<!-- 设置执行器最小核心线程数，默认为2；taskStrategy为0时设置稍微大一点，比如10-->
		<property name="threadCoreSize" value="5"/>
		<!-- 设置执行器最大核心线程数，默认为5；taskStrategy为0时设置稍微大一点，比如20-->
		<property name="threadMaxSize" value="10"/>
		<!-- 设置总调度开关,默认true,支持动态调整 -->
		<property name="scheduleFlag" value="true"/>
		<!-- 设置启用巡航模式，默认true,支持动态调整 -->
		<property name="enableNavigate" value="true"/>
		<!-- 设置启用反压模式，默认false,支持动态调整 -->
		<property name="enableBackFetch" value="false"/>
</bean>
	
```
其中，zkConnectAddress需要是tiger-service提供的zk地址.
#### 3.3 配置spring bean: dispatchTaskService
  
a) 上述3.1中``<property name="taskStrategy" value="0"/>``如果配置为统一捞取任务策略(single)，则在spring xml中配置:

```
<bean id="dispatchTaskService" 
		class="com.dianping.tiger.core.impl.DispatchTaskServiceSingleClientImpl"/>
```
b) 上述3.1中``<property name="taskStrategy" value="1"/>``如果配置为各个执行器各自捞取任务策略(multi)，则在spring xml中配置:

```
<bean id="dispatchTaskService" 
		class="com.dianping.tiger.core.impl.DispatchTaskServiceMultiClientImpl"/>
```

#### 3.4 配置spring bean: com.dianping.tiger.core.TigerTaskUtil

在spring xml中配置TigerTaskUtil，此工具类提供静态方法，用于添加或取消一个任务

```
<bean class="com.dianping.tiger.core.TigerTaskUtil">
		<property name="dispatchTaskService" ref="dispatchTaskService"/>
	</bean>
```
比如，要添加一个任务，使用如下方法（具体的注释及其它方法请看内部实现）：

```
TigerTaskUtil.addTask(String handler, Date executeTime, int loadbalance,String params, String bizUniqueId);
```

#### 3.5 引入spring-tiger.xml
在appServer的applicationContext.xml中引入spring-tiger.xml

```
<import resource="classpath:/META-INF/spring/spring-tiger.xml"/>
```

#### 3.6 实现任务分发接口
在appServer中实现如下接口：

```
com.dianping.tiger.engine.dispatch.DispatchHandler
```
这里用于实现 ***业务逻辑***，可以参考tiger-service里的demoHandler，并在 **spring xml**里配置你的实现类;

任务分发支持并行、串行两种执行策略。 默认是并行执行策略，如果需要串行执行策略（同一个任务有先后执行顺序的情况下）,在实现的类里增加一个注解,如：

```
@ExecuteType(AnnotationConstants.Executor.CHAIN)
public class ChainTestHandler implements DispatchHandler {
    @Override
    public DispatchResult invoke(DispatchParam param) throws Exception {
        Long taskId =  param.getTaskId();
        String jsonStr = param.getBizParameter();
        Map<String, String> paramMap = (Map<String, String>) JSON.parse(jsonStr);
        ...
    }
}
```
##### 注意点
1) 上述3.1中``<property name="handlers" value="demoHandler1,demoHandler2"/>``中的handler名字需要和DispatchHandler接口实现类的 **bean名字**一样，执行器handler之间用,分隔;

2) DispatchHandler接口实现类的spring bean配置默认是 单例，所以在实现类里最好 **不用成员变量**，而要用局部变量， **成员变量是有状态的，会有线程安全问题**;

3) param.getBizParameter()的类型与你之前addTask时候的入参类型保持一致，tiger不会做任何转换;

#### 3.7 启动应用
完成如上步骤，启动应用，查看tiger启动日志，看到红线标注部分(start success)，代表启动成功，如图：

![startlog](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/startlog.png)
  
#### 注：
对业务应用方来说，要接入只需完成上面的 **[3.1~3.7]**步骤即可。

## ======Tiger任务动态加载Groovy======
自 ***1.2.0*** 版本起，tiger支持任务代码的动态修改，通过groovy来实现， **但不是很建议用**

### groovy动态加载接入说明
1 配置启用groovy动态加载开关，上述3.1中加入如下配置:

```
<property name="enableGroovyCode" value="true"/>

```
2 实现groovy code操作接口

```
com.dianping.tiger.engine.groovy.IGroovyCodeRepo
```

3 接下来实现任务分发接口（同step 3.6）

**groovy特别说明**

1) groovy代码中service注入方式

```
import com.dianping.tiger.engine.annotation.TService;
@TService
private WedSmsSendService wpsWedSmsSendService;
```
2) groovy代码支持单例和多例两种方式，默认为多例，若需要使用单例，则采用如下注解的形式

```
import com.dianping.tiger.engine.annotation.AnnotationConstants;
import com.dianping.tiger.engine.annotation.GroovyBeanType;

@GroovyBeanType(AnnotationConstants.BeanType.SINGLE)
class GroovyTest implements DispatchHandler {
}
```

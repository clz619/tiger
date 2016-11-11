# tiger说明-单体式部署方式

单体式部署的jar依赖如下图：

![dantishi](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/dantishi.png)

其中，

**tiger-engine**是核心组件；

**tiger-core**提供addDispatchTask接口的client实现，供其它应用调用(或rpc调用)；

**tiger-biz**主要用来与任务储存打交道；

**tiger-service**是war包，进行服务部署，handler的执行代码写在这里。

## ======Quick Start======
### Step一. 环境配置
本项目在如下环境下运行良好

1）容器：tomcat 6.0及以上

2）zookeeper服务：zookeeper-3.3.6版本

3）mysql: mysql-5.5.49版本


### Step二. 个性化更改

1）数据源：配置在tiger-biz里的spring-db-config.xml，可替换c3p0DataSource；

2）orm层：项目用的是ibatis，如有不同，可改用其它orm框架;

3）任务表结构：在tiger-biz里的tables_mysql_tiger.sql，建议做适当的索引；

4）日志输出：log4j.xml可以根据自己的需求定义，但请保证tiger框架日志(appender name="TIGER")的输出。

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

### Step三. 项目启动
#### 3.1 依赖tiger-core
```
<dependency>
    <groupId>com.dianping</groupId>
    <artifactId>tiger-core</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```


#### 3.2 配置tigerConfigManager
在tiger-service中的spring-handler.xml中配置tigerConfigManager

```
<bean id="tigerConfigManager" class="com.dianping.tiger.core.TigerConfigManager">
		<!-- 必须,设置20s轮询一次任务 -->
		<property name="interval" value="20"/>
		<!-- 必须,设置zk集群地址 -->
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
		<!-- 设置最小核心线程数，默认为2；taskStrategy为0时设置稍微大一点，比如10-->
		<property name="ThreadCoreSize" value="5"/>
		<!-- 设置最大核心线程数，默认为5；taskStrategy为0时设置稍微大一点，比如20-->
		<property name="ThreadMaxSize" value="10"/>
		<!-- 设置总调度开关,默认true,支持动态调整 -->
		<property name="scheduleFlag" value="true"/>
		<!-- 设置启用巡航模式，默认true,支持动态调整 -->
		<property name="enableNavigate" value="true"/>
		<!-- 设置启用反压模式，默认false,支持动态调整 -->
		<property name="enableBackFetch" value="false"/>
</bean>
	
```

#### 3.3 配置spring bean: dispatchTaskService
  
a)上述3.1中``<property name="taskStrategy" value="0"/>``如果配置为统一捞取任务策略(single)，则在tiger-service中的spring-handler.xml中配置

```
<bean id="dispatchTaskService" 
		class="com.dianping.tiger.core.impl.DispatchTaskServiceSingleClientImpl"/>
```
b) 上述3.1中``<property name="taskStrategy" value="1"/>``如果配置为各个执行器各自捞取任务策略(multi)，则在tiger-service中的spring-handler.xml中配置

```
<bean id="dispatchTaskService" 
		class="com.dianping.tiger.core.impl.DispatchTaskServiceMultiClientImpl"/>
```

#### 3.4 引入spring-tiger.xml
在tiger-service中的applicationContext.xml中引入spring-tiger.xml

```
<import resource="classpath:/META-INF/spring/spring-tiger.xml"/>
```

#### 3.5 实现任务分发接口
在tiger-service中的``com.dianping.tiger.service.handler``包下，实现如下接口：

```
com.dianping.tiger.engine.dispatch.DispatchHandler
```
这里用于实现 ***业务逻辑***，可以参考里面的demoHandler，并在 **spring-handler.xml**里配置你的实现类;

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

#### 3.6 启动应用
完成如上步骤，先启动zookeeper服务，再启动应用，查看tiger启动日志，看到红线标注部分(start success)，代表启动成功，如图：

![startlog](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/startlog.png)
  

## ======Tiger任务动态加载Groovy======
自 ***1.2.0*** 版本起，tiger支持任务代码的动态修改，通过groovy来实现，但不是很建议用

### groovy动态加载接入说明
1 配置启用groovy动态加载开关，上述3.1中加入如下配置:

```
<property name="enableGroovyCode" value="true"/>

```
2 实现groovy code操作接口

```
com.dianping.tiger.engine.groovy.IGroovyCodeRepo
```

3 接下来实现任务分发接口（同step 3.4）

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

## ======Tiger监控======
tiger应用运行期间，支持任务监控，部署tiger-monitor;
并且，在tiger应用中，上述3.1中增加如下两个配置：监控地址和监控开关

```
<property name="monitorUrl" value="http://127.0.0.1:8080"/>

<property name="enableMonitor" value="true"/>

```
**注意点:**
tiger监控用的是文件存储方式，需要对/data/appdatas/tiger/目录有读写权限

tiger监控截图：

![monitor](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/monitor.png)


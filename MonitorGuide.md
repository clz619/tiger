# tiger说明-监控部署
tiger的监控包括任务统计和任务跟踪，任务统计主要用于查看某段时间内某个handler的运行总体情况，任务跟踪主要用于查看handler的运行详细情况。

任务统计部分用的mysql存储，任务跟踪部分用的elasticsearch，在部署tiger-monitor-web前，需要配置如下环境。

### 一、db相关配置
1) 数据源：配置在tiger-monitor里的applicationcontext-database，可替换c3p0DataSource；

2) 任务统计表、报警规则表结构：在tiger-monitor里的tables_mysql_tiger_monitor.sql，建议做适当索引；

### 二、es相关配置
1) es属性设置

在tiger-monitor-web里的applicationcontext-biz.xml中，对bean monitorDetailEsManager设置合适的clusterName和esServers，如:

```
<bean id="monitorDetailEsManager" 	 class="com.dianping.tiger.monitor.es.MonitorDetailEsManager" 
	init-method="init" destroy-method="destroy">
		<!-- 设置es集群名 -->
     	<property name="clusterName" value="xxx-es"/>
     	<!-- 设置es集群地址 -->
     	<property name="esServers" value="192.168.224.139:9300;127.0.0.1:9300"/>
 </bean>
```
elasticsearch服务的部署请查看[es入门博客](https://my.oschina.net/tkyuan/blog/733494)

2）es mapping结构设置

建立es的mapping结构，具体语句请查看tiger-monitor-web里的es-mapping文件。

完成以上一、二两步后，启动tiger-monitor-web，即完成部署。

**注:** elasticsearch的运行需要在 **jdk1.7及以上版本**，所以tiger-monitor-web的编译部署需要jdk1.7版本及以上。

==========此外，要让监控数据进来，还需以下应用的配置==========

### 三、tiger应用配置[任务统计]监控

在tiger的单体式或微服务部署的说明里，在步骤配置bean tigerConfigManager中，增加如下两个配置：监控地址和监控开关

```
<bean id="tigerConfigManager" class="com.dianping.tiger.core.TigerConfigManager">
	...
	<!-- 设置监控接收地址 -->
	<property name="monitorUrl" value="http://127.0.0.1:8080"/>
	<!-- 设置启用监控，默认false,支持动态调整 -->
	<property name="enableMonitor" value="true"/>
</bean>
```

### 四、tiger应用配置[任务跟踪]监控
在tiger-biz的spring-biz.xml中，对bean monitorDetailManager设置合适的监控地址和监控开关（同上）

```
<bean id="monitorDetailManager" class="com.dianping.tiger.biz.task.monitor.MonitorDetailManager">
		<!-- 设置监控接收地址 -->
		<property name="monitorUrl" value="http://127.0.0.1:8080" />
		<!-- 设置启用监控，默认false,支持动态调整 -->
		<property name="enableMonitor" value="true" />
</bean>
```
完成以上三、四两步，就能看到监控情况了。
任务统计监控截图：

![monitorrecord](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/monitorrecord.png)

任务跟踪监控截图：

![monitordetail](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/monitordetail.png)

### 五、tiger任务报警

除了对运行任务进行统计监控和任务跟踪外，还支持任务报警，对某些执行失败的任务可以配置报警规则。tiger任务执行失败重试，最多重试60次，可以根据这个约束配置报警规则，如图所示：
![monitoralarm](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/monitoralarm.png)

### 六、tiger集群注册
tiger支持双向注册，可以在监控页面看到集群注册信息，如图所示：

![monitorregister](https://github.com/tkyuan/tiger/blob/master/tiger-service/src/main/resources/img/monitorregister.png)


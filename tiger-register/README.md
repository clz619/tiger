## tiger-register引入说明

### 1. 定义spring bean
```
<bean id="tigerRegisterManager" 
		class="com.dianping.tiger.register.TigerRegisterManager" init-method="init"/>
<bean id="tigerRegisterDao" 
		class="com.dianping.tiger.register.dao.impl.TigerRegisterDaoImpl" parent="baseDao"/>
```

### 2. 引入sqlmap
```
<sqlMap resource="META-INF/sqlmap/register/tigerRegister.xml"/>
```

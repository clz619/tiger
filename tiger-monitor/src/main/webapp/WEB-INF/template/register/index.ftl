<!DOCTYPE html>
<html>
<head>
	<title>Tiger Center</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<#import "/common/common.macro.ftl" as commonMacro>
    <link rel="stylesheet" type="text/css" href="/static/js/easyui/themes/gray/easyui.css">
    <style>
    	.combo-text{width:140px;}
    </style>
</head>

<body>

<div class="easyui-layout" style="width:100%;height:1050px;">
	<div region="north" title="任务组" style="height:60px;">
		<span style="padding:5px;">handlerGroup:</span>
		<select style="padding:10px;" id="J_handlerGroup" onchange=reload()>
			<option value="">未选择</option>
			<#if handlerGroupList?exists && handlerGroupList?size gt 0 >
			<#list handlerGroupList as hg>
			<option value="${hg}" <#if hg==handlerGroup>selected</#if>>${hg}</option>
			</#list>
			</#if>
		</select>
	</div>
	<!-- left menu-->
	<@commonMacro.commonLeft />
	
	<!-- 正文 -->
	<div region="center" style="padding:10px;height:900px;">
		<hr>
	    <#-- 列表 -->
        <table id="dg" title2="集群注册" style2="height:100%;width:100%;" ></table>
		
		<span style="position:absolute;bottom:5px;">Copyright © 2017 Dianping</span>
	</div>
	
	<!-- foot
	<div region="south" style="padding:5px;height:30px;">
	</div>
	 -->
</div>


<!-- jquery -->
<script type="text/javascript" src="/static/plugin/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/static/plugin/jquery/jquery.validate.min.js"></script>
<!-- easyui -->
<script type="text/javascript" src="/static/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/js/easyui/locale/easyui-lang-zh_CN.js"></script>

<!-- current page js -->
<script type="text/javascript" src="/static/js/monitor/index.register.js"></script>

</body>
</html>

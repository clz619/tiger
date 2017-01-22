<!DOCTYPE html>
<html>
<head>
	<title>Tiger Center</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
    <link rel="stylesheet" type="text/css" href="/static/js/easyui/themes/gray/easyui.css">
    <style>
    	.combo-text{width:140px;}
    </style>
</head>

<body>

<div class="easyui-layout" style="width:100%;height:780px;">
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
	<!-- menu <a href="/tiger">任务统计</a> -->
	<div region="west" split="true" title="常用模块" style="width:187px;">
		<div style="width:180px;height:auto;">
			<div class="easyui-panel" title="监控统计" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger?handlerGroup=${handlerGroup}">任务统计</a></p>
			</div>
			<div class="easyui-panel" title="任务跟踪" collapsible="true" collapsed="true" style="height:auto;padding:5px;">
				<p><a href="/tiger/trace?handlerGroup=${handlerGroup}">任务跟踪</a></p>
			</div>
		</div>
	</div>
	<!-- 正文 -->
	<div region="center" style="padding:10px;height:650px;">
		<!-- 刷选条件框 -->
		<div id="tb" style="padding:5px;height:30px;">
			<span style="padding:5px;">handler:</span>
			<select style="padding:5px;" id="J_handlerName">
				<option value="">未选择</option>
				<#if handlerNameList?exists && handlerNameList?size gt 0 >
				<#list handlerNameList as hn>
				<option value="${hn}">${hn}</option>
				</#list>
				</#if>
			</select>
                                
			<span style="padding:10px;">执行时间:</span>
			<input id="J_monitorTimeFrom" class="easyui-datetimebox" style="width:150px;" editable="false" />
			至
			<input id="J_monitorTimeTo" class="easyui-datetimebox" style="width:150px;" editable="false" />
			
			<a id="J_btnSearch" href="#" class="easyui-linkbutton" style="width:60px;">查 询</a>
		</div>
		
		<hr>
	    <!-- 执行次数 -->
		<div id="J_container_count" style="min-width: 250px; height: 280px; margin: 0 auto"></div>
		<hr>
		<!-- 执行耗时 -->
		<div id="J_container_cost" style="min-width: 250px; height: 280px; margin: 0 auto"></div>
		
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

<!-- Highcharts -->
<script type="text/javascript" src="/static/plugin/Highcharts-4.1.8/highcharts.js"></script>
<script type="text/javascript" src="/static/plugin/Highcharts-4.1.8/modules/exporting.js"></script>
<!-- current page js -->
<script type="text/javascript" src="/static/js/monitor/index.record.js"></script>

</body>
</html>

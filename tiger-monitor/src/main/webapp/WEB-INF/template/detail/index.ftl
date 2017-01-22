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
	<!-- menu <a href="/tiger">任务统计</a> -->
	<div region="west" split="true" title="常用模块" style="width:187px;">
		<div style="width:180px;height:auto;">
			<div class="easyui-panel" title="监控统计" collapsible="true" collapsed="true" style="height:auto;padding:5px;">
				<p><a href="/tiger?handlerGroup=${handlerGroup}">任务统计</a></p>
			</div>
			<div class="easyui-panel" title="任务跟踪" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger/trace?handlerGroup=${handlerGroup}">任务跟踪</a></p>
			</div>
		</div>
	</div>
	<!-- 正文 -->
	<div region="center" style="padding:10px;height:900px;">
		<!-- 刷选条件框 -->
		<div id="tb" style="padding:5px;height:30px;">
			<span style="padding:5px;">业务参数:</span>
			<input type="text" id="J_bizParam"/>
			<span style="padding:10px;">任务id:</span>
			<input type="text" id="J_taskId" class="easyui-numberbox" data-options="min:1" placeholder="仅限数字">
			<span style="padding:10px;">任务跟踪ttid:</span>
			<input type="text" id="J_ttid"/>
			
			<a id="J_btnSearch" href="#" class="easyui-linkbutton" style="width:60px;">查 询</a>
		</div>
		
		<hr>
	    <#-- 列表 -->
        <table id="dg" title2="任务跟踪" style2="height:100%;width:100%;" ></table>
		
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
<script type="text/javascript" src="/static/js/monitor/index.detail.js"></script>

</body>
</html>

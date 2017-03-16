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
			
			<a id="J_btnSearch" href="#" class="easyui-linkbutton" style="width:60px;margin-left:10px;">查 询</a>
			<a id="J_btnAdd" href="#" class="easyui-linkbutton" style="width:60px;margin-left:300px;">添加报警 </a>
			<a id="J_btnEdit" href="#" class="easyui-linkbutton" style="width:40px;margin-left:10px;">修  改</a>
			<a id="J_btnDelete" href="#" class="easyui-linkbutton" style="width:40px;margin-left:10px;color:red;">删  除</a>
		</div>
		
		<hr>
	    <#-- 列表 -->
        <table id="dg" title2="任务报警" style2="height:100%;width:100%;" ></table>
		
		<span style="position:absolute;bottom:5px;">Copyright © 2017 Dianping</span>
	</div>
	
	<!-- foot
	<div region="south" style="padding:5px;height:30px;">
	</div>
	 -->
</div>

<!-- add window -->
<div id="J_addWin" title="新增一个配置" class="easyui-window"  data-options="modal:true,closed:true,resizable:false" style="width:600px;">
    <form class="form" style="padding:10px 10px 10px 20px;">
    	<input type="hidden" name="handlerGroup" value="${handlerGroup}">
        <table>
            <tr style="height: 30px;">
                <td>handler：<span style="color:red;margin-left: 5px;">*</span></td>
                <td>
                    <input type="text" name="handlerName" style="width:160px;" placeholder="请输入handlerName" >
                </td>
                <td style="padding-left: 20px;">开启报警：<span style="color:red;margin-left: 5px;">*</span></td>
                <td>
                是<input type="radio" name="offFlag" value="0"/>
                否<input type="radio" name="offFlag" value="1"/>
                </td>
            </tr>
            <tr style="height: 30px;">
                <td>报警触发规则：<span style="color:red;margin-left: 5px;">*</span></td>
                <td colspan="3">连续执行失败
                <input type="text" name="leastFailNum" style="width:40px;" class="easyui-numberbox" data-options="min:1" placeholder="限数字">次后触发报警，
                接下来每隔
                <input type="text" name="intervalFailNum" style="width:40px;" class="easyui-numberbox" data-options="min:1" placeholder="限数字">次失败触发报警
                </td>
            </tr>
            <tr style="height: 30px;">
                <td>报警联系邮箱：<span style="color:red;margin-left: 5px;">*</span></td>
                <td colspan="3">
                	<textarea name="mailReceives" maxlength="256" style="width:350px;height:50px;" placeholder="请输入报警邮箱，多个邮箱用;分隔"></textarea>
                </td>
            </tr>
        </table>
        <div style="padding:5px;text-align:center;">
            <a href="#" class="easyui-linkbutton" id="J_addOk">保存</a>
            <a href="#" class="easyui-linkbutton" id="J_addReset">重置</a>
        </div>
    </form>
</div>

<!-- edit window -->
<div id="J_editWin" title="编辑配置" class="easyui-window"  data-options="modal:true,closed:true,resizable:false" style="width:600px;">
    <form class="form" style="padding:10px 10px 10px 20px;">
    	<input type="hidden" name="handlerGroup" value="${handlerGroup}">
    	<input type="hidden" name="id">
        <table>
            <tr style="height: 30px;">
                <td>handler：</td>
                <td>
                    <input type="text" name="handlerName" style="width:160px;" placeholder="请输入handlerName" readonly>
                </td>
                <td style="padding-left: 20px;">开启报警：<span style="color:red;margin-left: 5px;">*</span></td>
                <td>
                是<input type="radio" name="offFlag" value="0"/>
                否<input type="radio" name="offFlag" value="1"/>
                </td>
            </tr>
            <tr style="height: 30px;">
                <td>报警触发规则：<span style="color:red;margin-left: 5px;">*</span></td>
                <td colspan="3">连续执行失败
                <input type="text" name="leastFailNum" style="width:40px;" class="easyui-numberbox" data-options="min:1" placeholder="限数字">次后触发报警，
                接下来每隔
                <input type="text" name="intervalFailNum" style="width:40px;" class="easyui-numberbox" data-options="min:1" placeholder="限数字">次失败触发报警
                </td>
            </tr>
            <tr style="height: 30px;">
                <td>报警联系邮箱：<span style="color:red;margin-left: 5px;">*</span></td>
                <td colspan="3">
                	<textarea name="mailReceives" maxlength="256" style="width:350px;height:50px;" placeholder="请输入报警邮箱，多个邮箱用;分隔"></textarea>
                </td>
            </tr>
        </table>
        <div style="padding:5px;text-align:center;">
            <a href="#" class="easyui-linkbutton" id="J_editOk">保存</a>
            <a href="#" class="easyui-linkbutton" id="J_editCancel">取消</a>
        </div>
    </form>
</div>

<!-- jquery -->
<script type="text/javascript" src="/static/plugin/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/static/plugin/jquery/jquery.validate.min.js"></script>
<!-- easyui -->
<script type="text/javascript" src="/static/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/js/easyui/locale/easyui-lang-zh_CN.js"></script>

<!-- current page js -->
<script type="text/javascript" src="/static/js/monitor/index.alarm.js"></script>

</body>
</html>

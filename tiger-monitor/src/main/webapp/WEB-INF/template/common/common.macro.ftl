<#macro commonStyle>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/static/js/easyui/themes/gray/easyui.css">
</#macro>

<#macro commonScript>
	<!-- jquery -->
	<script type="text/javascript" src="/static/plugin/jquery/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="/static/plugin/jquery/jquery.validate.min.js"></script>
	<!-- easyui -->
	<script type="text/javascript" src="/static/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/static/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <!-- scrollup
    <script src="${request.contextPath}/static/plugins/scrollup/jquery.scrollUp.min.js"></script>
    
     -->
</#macro>

<#macro commonHeader>
	<header class="main-header">
		<a href="${request.contextPath}/" class="logo">
			<span class="logo-mini"><b>FRI</b></span>
			<span class="logo-lg"><b>任务调度中心</b></span>
		</a>
		<nav class="navbar navbar-static-top" role="navigation">
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"><span class="sr-only">切换导航</span></a>
			<div class="navbar-custom-menu"></div>
		</nav>
	</header>
</#macro>

<#macro commonLeft>
	<div region="west" split="true" title="常用模块" style="width:187px;">
		<div style="width:180px;height:auto;">
			<div class="easyui-panel" title="监控统计" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger?handlerGroup=${handlerGroup}">任务统计</a></p>
			</div>
			<div class="easyui-panel" title="任务跟踪" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger/trace?handlerGroup=${handlerGroup}">任务跟踪</a></p>
			</div>
			<div class="easyui-panel" title="任务报警" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger/alarm?handlerGroup=${handlerGroup}">任务报警</a></p>
			</div>
			<div class="easyui-panel" title="集群注册" collapsible="true" collapsed="false" style="height:auto;padding:5px;">
				<p><a href="/tiger/register?handlerGroup=${handlerGroup}">集群注册</a></p>
			</div>
		</div>
	</div>
</#macro>

<#macro commonFooter >
	<footer class="main-footer">
		
		Copyright &copy; 2016 Dianping
			<!--
			<div class="pull-right hidden-xs">
			<b>Version</b> 1.0
		</div>
			<strong>
			<a href="https://github.com/xuxueli/xxl-job" target="_blank" >github</a>&nbsp;
			<a href="http://www.cnblogs.com/xuxueli/p/5021979.html" target="_blank" >cnblog</a>.
			
		</strong> 
		-->
	</footer>
</#macro>

<#macro comAlert >
	<!-- ComAlert.模态框Modal -->
	<div class="modal fade" id="ComAlert" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!--	<div class="modal-header"><h4 class="modal-title"><strong>提示:</strong></h4></div>	-->
	         	<div class="modal-body"><div class="alert alert-success"></div></div>
	         	<div class="modal-footer">
	         		<div class="text-center" >
	            		<button type="button" class="btn btn-default ok" data-dismiss="modal" >确认</button>
	            	</div>
	         	</div>
			</div>
		</div>
	</div>
	<!-- ComConfirm.模态框Modal -->
	<div class="modal fade" id="ComConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
	         	<div class="modal-body"><div class="alert alert-success"></div></div>
	         	<div class="modal-footer">
	         		<div class="text-center" >
	            		<button type="button" class="btn btn-primary ok" data-dismiss="modal" >确认</button>
	            		<button type="button" class="btn btn-default cancel" data-dismiss="modal" >取消</button>
	            	</div>
	         	</div>
			</div>
		</div>
	</div>
	<!-- ComAlertTec.模态框Modal -->
	<div class="modal fade" id="ComAlertTec" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content-tec">
	         	<div class="modal-body"><div class="alert" style="color:#fff;"></div></div>
	         	<div class="modal-footer">
	         		<div class="text-center" >
	            		<button type="button" class="btn btn-info ok" data-dismiss="modal" >确认</button>
	            	</div>
	         	</div>
			</div>
		</div>
	</div>
	<script>
		// 通用提示
		var ComAlert = {
			show:function(type, msg, callback){
				// 弹框初始
				if (type == 1) {
					$('#ComAlert .alert').attr('class', 'alert alert-success');
				} else {
					$('#ComAlert .alert').attr('class', 'alert alert-warning');
				}
				$('#ComAlert .alert').html(msg);
				$('#ComAlert').modal('show');
				
				$('#ComAlert .ok').click(function(){
					$('#ComAlert').modal('hide');
					if(typeof callback == 'function') {
						callback();
					}
				});
				
				// $("#ComAlert").on('hide.bs.modal', function () {	});	// 监听关闭
			}
		};
		// 通用确认弹框
		var ComConfirm = {
			show:function(msg, callback){
				// 弹框初始
				$('#ComConfirm .alert').attr('class', 'alert alert-warning');
				$('#ComConfirm .alert').html(msg);
				$('#ComConfirm').modal('show');
				
				$('#ComConfirm .ok').unbind("click");	// 解绑陈旧事件
				$('#ComConfirm .ok').click(function(){
					$('#ComConfirm').modal('hide');
					if(typeof callback == 'function') {
						callback();
						return;
					}
				});
				
				$('#ComConfirm .cancel').click(function(){
					$('#ComConfirm').modal('hide');
					return;
				});
			}
		};
		// 提示-科技主题
		var ComAlertTec = {
			show:function(msg, callback){
				// 弹框初始
				$('#ComAlertTec .alert').html(msg);
				$('#ComAlertTec').css({
					width: 'auto'
				}).modal({backdrop: 'static', keyboard: false}).modal('show');
				
				$('#ComAlertTec .ok').click(function(){
					$('#ComAlertTec').modal('hide');
					if(typeof callback == 'function') {
						callback();
					}
				});
			}
		};
	</script>
</#macro>
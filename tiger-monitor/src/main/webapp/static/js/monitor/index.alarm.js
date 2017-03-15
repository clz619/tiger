$(function () {
	
	// datagrid 属性
    $('#dg').datagrid({
        url:'/tiger/pageListMonitorAlarm',
        queryParams:{
        	handlerName: $("#J_handlerName").val(),
        	handlerGroup:$("#J_handlerGroup").val()
        },
        pagination:true,
        pageSize: 20, 				//读取分页条数，即向后台读取数据时传过去的值
        pageList: [20, 50, 100], //可以调整每页显示的数据，即调整pageSize每次向后台请求数据时的数据
        rownumbers:false,
        singleSelect:true,
        fitColumns: true, 			// 设置为true将自动使列适应表格宽度以防止出现水平滚动,false则自动匹配大小
        nowrap:false,
        columns:[[
            {field:'id', title:'报警配置id', width:15,hidden:true},
            {field:'handlerName', title:'配置handler', width:35},
            {field:'updateTime', title:'上次更新时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'alarmDesc', title:'报警规则描述', width:60,
                formatter:function(value, row, index) {
                    return '连续执行失败'+'<span style="color:red;">' +row.leastFailNum+ '</span>'+'次后触发报警，接下来每隔'
                    +'<span style="color:blue;">' +row.intervalFailNum+ '</span>' + '次失败触发报警';
                }
            },
            {field:'mailReceives', title:'报警联系人', width:50},
            {field:'offFlag', title:'报警开关', width:15,
                formatter:function(value, row, index) {
                    if (value == 0) {
                        return '<span style="color:blue;">开启</span>';;
                    }else if(value == 1){
                    	return '<span style="color:red;">关闭</span>';
                    }
                    return value;
                }
            }
        ]]
    });
	
	//ajax query
	$("#J_btnSearch").click(function(){
		var handlerGroup = $("#J_handlerGroup").val();
		if (!handlerGroup || handlerGroup == '') {
			$.messager.alert('提示','请选择任务组','warning');
			return;
		}
		
		$("#dg").datagrid("load", {
			handlerName: $("#J_handlerName").val(),
			handlerGroup:$("#J_handlerGroup").val()
        });
		
	});
	
	//==========open add window
	$("#J_btnAdd").click(function(){
		$("#J_addReset").click();
		$("#J_addWin").window('open');
	});
	
	//reset add window
	$("#J_addReset").click(function(){
        $("#J_addWin form ").form('reset');
    });
	
	//add ajax
	$("#J_addOk").click(function(){
		
		var handlerGroup = $("#J_addWin form input[name=handlerGroup]").val();
        var handlerName = $("#J_addWin form input[name=handlerName]").val();
        var offFlag = $("#J_addWin form input[name=offFlag]:checked").val();
        var leastFailNum = $("#J_addWin form input[name=leastFailNum]").val();
        var intervalFailNum = $("#J_addWin form input[name=intervalFailNum]").val();
        var mailReceives = $("#J_addWin form textarea[name=mailReceives]").val();
        
		$("#J_addWin").window('close');
		
		$.ajax({
	        method: 'POST',
	        url: "/tiger/addMonitorAlarm",
	        data: {
	        	handlerGroup : handlerGroup,
	        	handlerName : handlerName,
	        	offFlag:offFlag,
	        	leastFailNum:leastFailNum,
	        	intervalFailNum:intervalFailNum,
	        	mailReceives:mailReceives
            },
	        success: function (data) {
	            var _data = data || {};
	            if (_data.code == 200) {
	                $.messager.alert('提示', '新增成功！', 'info', function(){
	                    $("#dg").datagrid("reload");
	                });
	            } else {
	                $.messager.alert('提示', '新增失败！'+ _data.msg, 'warning');
	            }
	        }
	    });
		
	});
	
	//====open edit window=======
    $('#J_btnEdit').click(function(){
        var rows = $('#dg').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选中一条记录', 'warning');
            return;
        }
        var item = rows[0];

        $('#J_editWin .form').form('load',item);
        $('#J_editWin').window('open');
    });
    
    //edit cancel
    $('#J_editCancel').click(function(){
    	$("#J_editWin").window('close');
    });
    
    //edit ajax
	$("#J_editOk").click(function(){
		
		var id = $("#J_editWin form input[name=id]").val();
        var offFlag = $("#J_editWin form input[name=offFlag]:checked").val();
        var leastFailNum = $("#J_editWin form input[name=leastFailNum]").val();
        var intervalFailNum = $("#J_editWin form input[name=intervalFailNum]").val();
        var mailReceives = $("#J_editWin form textarea[name=mailReceives]").val();
        
		$("#J_editWin").window('close');
		
		$.ajax({
	        method: 'POST',
	        url: "/tiger/updateMonitorAlarm",
	        data: {
	        	id : id,
	        	offFlag:offFlag,
	        	leastFailNum:leastFailNum,
	        	intervalFailNum:intervalFailNum,
	        	mailReceives:mailReceives
            },
	        success: function (data) {
	            var _data = data || {};
	            if (_data.code == 200) {
	                $.messager.alert('提示', '更新成功！', 'info', function(){
	                    $("#dg").datagrid("reload");
	                });
	            } else {
	                $.messager.alert('提示', '更新失败！'+ _data.msg, 'warning');
	            }
	        }
	    });
		
	});
	
	//delete ajax
	$("#J_btnDelete").click(function(){
		
		var rows = $('#dg').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选中一条记录', 'warning');
            return;
        }
        var item = rows[0];
		
        $.messager.confirm('提示', '确认进行删除操作？', function(data){

            if (data) {
                $.ajax({
                    method: 'POST',
                    url: "/tiger/deleteMonitorAlarm",
                    data: {
                        'id': item.id
                    },
                    success: function (data) {
                        var _data = data || {};
                        if (_data.code == 200) {
                            $.messager.alert('提示', '删除成功', 'info', function(){
                                $("#dg").datagrid("reload");
                            });
                        } else {
                            $.messager.alert('提示', '删除失败！'+ _data.msg, 'warn');
                        }
                    }
                });
            }
        });
    });
	
	
});

function reload(){
	//reload
	var handlerGroup = $('#J_handlerGroup').val();
	window.location.href = "/tiger/alarm?handlerGroup=" + handlerGroup;
}


//Format
Date.prototype.Format = function(fmt){ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


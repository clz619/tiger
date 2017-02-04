$(function () {
	
	// datagrid 属性
    $('#dg').datagrid({
        url:'/tiger/pageListMonitorDetail',
        queryParams:{
        	handlerName: $("#J_handlerName").val(),
        	handlerGroup:$("#J_handlerGroup").val(),
			bizParam: $("#J_bizParam").val(),
			taskId: $('#J_taskId').numberbox('getValue'),
			ttid: $("#J_ttid").val()
        },
        pagination:true,
        pageSize: 20, 				//读取分页条数，即向后台读取数据时传过去的值
        pageList: [20, 50, 100], //可以调整每页显示的数据，即调整pageSize每次向后台请求数据时的数据
        rownumbers:false,
        singleSelect:true,
        fitColumns: true, 			// 设置为true将自动使列适应表格宽度以防止出现水平滚动,false则自动匹配大小
        nowrap:false,
        columns:[[
            {field:'taskId', title:'任务ID', width:15},
            {field:'addTime', title:'生成时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'monitorTime', title:'执行时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'handler', title:'执行handler', width:35},
            {field:'node', title:'所在节点', width:15,hidden:true},
            {field:'retryTimes', title:'重试次数', width:15},
            {field:'status', title:'任务状态', width:15,
                formatter:function(value, row, index) {
                    if (value == 0) {
                        return '待执行';
                    }else if(value == 1){
                    	return '<span style="color:blue;">执行成功</span>';
                    }else if(value == 2){
                    	return '<span style="color:red;">放弃执行</span>';
                    }
                    return value;
                }
            },
            {field:'earliestExecuteTime', title:'下次执行时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'parameter', title:'业务参数', width:35,
            	formatter:function(value, row, index) {
                    return '<textarea id="J_row'+index+'" readonly ondblclick="showValue('+index+')">'+value+'</textarea>';
                }
            },
            {field:'host', title:'执行机器', width:25},
            {field:'ttid', title:'跟踪ttid', width:20},
            {field:'executeResult', title:'监控结果', width:15},
            
            {field:'_opt', title:'操作',hidden:true, width:5,
                formatter:function(value, row, index) {
                    var btn = '<a href="/mvc/shop/yzs/bookDetail?id='+row.id+'" target="_blank" style="color: #FF8400">查看</a>';
                    btn += '&nbsp;&nbsp;&nbsp;<a href="javascript:followLog('+row.id+');" class="followLog" style="color: #FF8400">跟进记录</a>';
                    return btn;
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
		var handlerName = $("#J_handlerName").val();
		var bizParam = $("#J_bizParam").val();
		var taskId = $('#J_taskId').numberbox('getValue');
		var ttid = $("#J_ttid").val();
		if((handlerName == null || handlerName =='') && (bizParam == null || bizParam =='') && (ttid == null || ttid =='') && (taskId == null || taskId=='')){
			$.messager.alert('提示','必须选择其中一个条件','warning');
			return;
		}
		
		$("#dg").datagrid("load", {
			handlerName: $("#J_handlerName").val(),
			handlerGroup:$("#J_handlerGroup").val(),
			bizParam: $("#J_bizParam").val(),
			taskId: $('#J_taskId').numberbox('getValue'),
			ttid: $("#J_ttid").val()
        });
		
	});
    
});

function reload(){
	//reload
	var handlerGroup = $('#J_handlerGroup').val();
	window.location.href = "/tiger/trace?handlerGroup=" + handlerGroup;
}

function showValue(value){
	var str = $('#J_row'+value).val();
	$.messager.show({
		title:'当前单元格信息',
		msg:str,
		width:400,
		height:150,
		showType:'show',
		timeout:30000,
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
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


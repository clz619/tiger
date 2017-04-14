$(function () {
	
	// datagrid 属性
    $('#dg').datagrid({
        url:'/tiger/pageListRegister',
        queryParams:{
        	handlerGroup:$("#J_handlerGroup").val()
        },
        pagination:false,
        pageSize: 20, 				//读取分页条数，即向后台读取数据时传过去的值
        pageList: [20, 50, 100], //可以调整每页显示的数据，即调整pageSize每次向后台请求数据时的数据
        rownumbers:false,
        singleSelect:true,
        fitColumns: true, 			// 设置为true将自动使列适应表格宽度以防止出现水平滚动,false则自动匹配大小
        nowrap:false,
        columns:[[
            {field:'id', title:'集群注册id', width:15,hidden:true},
            {field:'hostName', title:'任务机器', width:40},
            {field:'registerVersion', title:'注册版本', width:50},
            {field:'registerTime', title:'注册时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value*1000).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'nodes', title:'负责的虚拟节点', width:40,
            	formatter:function(value, row, index) {
                    return '<textarea style="margin: 1px; width: 100%; height: 100%;" id="J_row'+index+'" readonly ondblclick="showValue('+index+')">'+value+'</textarea>';
                }
            },
            {field:'updateTime', title:'最近更新时间', width:25,
                formatter:function(value, row, index) {
                    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {field:'version', title:'version', width:10}
        ]]
    });
	
});

function reload(){
	//reload
	var handlerGroup = $('#J_handlerGroup').val();
	window.location.href = "/tiger/register?handlerGroup=" + handlerGroup;
}

function showValue(value){
	var str = $('#J_row'+value).val();
	$.messager.show({
		title:'当前单元格信息',
		msg:str,
		width:500,
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


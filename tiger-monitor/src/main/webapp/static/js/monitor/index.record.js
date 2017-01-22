$(function () {
	
	//ajax query
	$("#J_btnSearch").click(function(){
		var handlerName = $("#J_handlerName").val();
		if (!handlerName || handlerName == '') {
			$.messager.alert('提示','请选择handler','warning');
			return;
		}
		var tmpTimeStartStr = $("#J_monitorTimeFrom").datetimebox('getValue');
		var tmpTimeEndStr = $("#J_monitorTimeTo").datetimebox('getValue');
		if(tmpTimeStartStr == null || tmpTimeStartStr =='' || tmpTimeEndStr == null || tmpTimeEndStr ==''){
			$.messager.alert('提示','请选择开始时间和结束时间','warning');
			return;
		}
		var tmpTimeStart = new Date(tmpTimeStartStr);
		var tmpTimeEnd = new Date(tmpTimeEndStr);
		if((tmpTimeEnd.getTime() < tmpTimeStart.getTime()) 
				|| ((tmpTimeEnd.getTime() - tmpTimeStart.getTime())> 24*60*60*1000)){
			$.messager.alert('提示','请正确选择开始时间和结束时间，且不能超过24小时','warning');
			return;
		}
		var data ={
			handlerGroup:$("#J_handlerGroup").val(),
			handlerName: $("#J_handlerName").val(),
			monitorTimeFrom: $("#J_monitorTimeFrom").datetimebox('getValue'),
			monitorTimeTo: $("#J_monitorTimeTo").datetimebox('getValue')
		};
		
		$.ajax({  
	         type:'post',      
	         url:'/tiger/monitorRecord',  
	         data:data,  
	         cache:false,  
	         dataType:'json',  
	         success:function(data){ 
	        	 //当有查找结果时
	        	 if(data != null){
	        		var chartCountData = new Array();
	        		var chartCostData = new Array();
	        		for(var key in data){
	        			var itemKey = key;
	        			var itemDataList = data[key];
	        			var itemValueList = new Array();
	        			for(var i in itemDataList){
	        				if(itemDataList[i].failNum > 0){
	        					itemValueList.push({
		        					x:itemDataList[i].monitorTime,
		        					y:itemDataList[i].totalNum,
		        					monitorTime:itemDataList[i].monitorTime,
		        					totalNum:itemDataList[i].totalNum,
		        					sucNum:itemDataList[i].sucNum,
		        					failNum:itemDataList[i].failNum,
		        					avgCost:itemDataList[i].avgCost,
		        					maxCost:itemDataList[i].maxCost,
		        					minCost:itemDataList[i].minCost,
		        					color:'red'
		        				});
	        				}else{
	        					itemValueList.push({
		        					x:itemDataList[i].monitorTime,
		        					y:itemDataList[i].totalNum,
		        					monitorTime:itemDataList[i].monitorTime,
		        					totalNum:itemDataList[i].totalNum,
		        					sucNum:itemDataList[i].sucNum,
		        					failNum:itemDataList[i].failNum,
		        					avgCost:itemDataList[i].avgCost,
		        					maxCost:itemDataList[i].maxCost,
		        					minCost:itemDataList[i].minCost
		        				});
	        				}
	        			}
	        			chartCountData.push({'name':itemKey, 'data':itemValueList});
	        			var itemValueList2 = new Array();
	        			for(var i in itemDataList){
	        				itemValueList2.push({
	        					x:itemDataList[i].monitorTime,
	        					y:itemDataList[i].avgCost,
	        					monitorTime:itemDataList[i].monitorTime,
	        					totalNum:itemDataList[i].totalNum,
	        					sucNum:itemDataList[i].sucNum,
	        					failNum:itemDataList[i].failNum,
	        					avgCost:itemDataList[i].avgCost,
	        					maxCost:itemDataList[i].maxCost,
	        					minCost:itemDataList[i].minCost
	        				});
	        			}
	        			chartCostData.push({'name':itemKey, 'data':itemValueList2});
	        		}
	        		fillChart(chartCountData,chartCostData);
	        	 }
	        	 
	         }  
	    });
	});
    
});

function reload(){
	//reload
	var handlerGroup = $('#J_handlerGroup').val();
	window.location.href = "/tiger?handlerGroup=" + handlerGroup;
}

function fillChart(chartCountData,chartCostData){
	// chart local
	Highcharts.setOptions({ global: { useUTC: false } });   // 时区处理
	//======== 执行次数 =======
    $('#J_container_count').highcharts({
        title: {
            text: '执行次数监控',
            x: -20 //center
        },
        xAxis: {
        	title: {text:'执行时间'},
        	labels: {
                formatter: function() {
                    return Highcharts.dateFormat('%H:%M:%S', this.value);                  
                },
                rotation: -45
            }
        },
        yAxis: {
        	title: {text:'执行次数'},
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
        	formatter:function(){
        		
        		var failPanel = this.point.failNum;
        		if (this.point.failNum > 0) {
        			failPanel = '<span style="color:red;">' + this.point.failNum +'</span>';
				}
        		
				return '<b>' + this.series.name + '</b>：<br>' + 
					'监控时间=' + Highcharts.dateFormat('%H:%M:%S',this.point.monitorTime) + ' <br>' +		// %Y-%m-%d %H:%M:%S
					'执行次数=' + this.point.totalNum + ' <br>' +
					'成功次数=' + this.point.sucNum + ' <br>' +
					'失败次数=' + failPanel + ' <br>' +
					'平均耗时=' + this.point.avgCost + ' <br>' +
					'max耗时=' + this.point.maxCost + ' <br>' +
					'min耗时=' + this.point.minCost;
			}
        },
        series:chartCountData 
    });
    
    //======== 执行耗时 ========
    $('#J_container_cost').highcharts({
        title: {
            text: '执行耗时监控',
            x: -20 //center
        },
        xAxis: {
        	title: {text:'执行时间'},
        	labels: {
                formatter: function() {
                    return Highcharts.dateFormat('%H:%M:%S', this.value);                  
                },
                rotation: -45
            }
        },
        yAxis: {
        	title: {text:'执行耗时/ms'},
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
        	formatter:function(){
        		
//        		var failPanel = this.point.failNum;
        		if (this.point.failNum > 0) {
        			failPanel = '<span style="color:red;">' + this.point.failNum +'</span>';
				}
        		
				return '<b>' + this.series.name + '</b>：<br>' + 
					'监控时间=' + Highcharts.dateFormat('%H:%M:%S',this.point.monitorTime) + ' <br>' +		// %Y-%m-%d %H:%M:%S
					/*'执行次数=' + this.y + ' <br>' +
					'成功次数=' + this.point.sucNum + ' <br>' +
					'失败次数=' + failPanel + ' <br>' +*/
					'平均耗时=' + this.point.avgCost + ' <br>' +
					'max耗时=' + this.point.maxCost + ' <br>' +
					'min耗时=' + this.point.minCost;
			}
        },
        series:chartCostData 
    });
    
}

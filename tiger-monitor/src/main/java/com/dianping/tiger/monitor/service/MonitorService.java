package com.dianping.tiger.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.dataobject.TigerMonitorRecordDo;
import com.dianping.tiger.monitor.vo.TigerDetailVo;

/**
 * monitor record
 * @author yuantengkai
 */
public interface MonitorService {
	
	/**
	 * 处理接收的监控数据
	 * @param originData
	 */
	public void dealStatisticsData(String originData);
	
	/**
	 * 处理接收的监控详情数据
	 * @param detailData
	 */
	public void dealDetailData(String detailData);

	/**
	 * 查询监控数据
	 * @param handlerGroup
	 * @param handlerName
	 * @param monitorTimeFrom
	 * @param monitorTimeTo
	 * @return key:hostName
	 */
	public Map<String, List<TigerMonitorRecordDo>> queryMonitorData(String handlerGroup, String handlerName,
												Date monitorTimeFrom, Date monitorTimeTo);

	/**
	 * 查询一段时间的handler列表
	 * @param handlerGroup
	 * @param monitorTimeFrom
	 * @param monitorTimeTo
	 * @return
	 */
	public List<String> queryMonitorHandlers(String handlerGroup, Date monitorTimeFrom,Date monitorTimeTo);
	
	/**
	 * 查询一段时间的handlerGroup列表
	 * @param monitorTimeFrom
	 * @param monitorTimeTo
	 * @return
	 */
	public List<String> queryMonitorHandlerGroups(Date monitorTimeFrom,Date monitorTimeTo);
	
	
	/**
	 * 查询任务执行跟踪情况
	 * @param handlerGroup
	 * @param handlerName
	 * @param bizParam
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageModel<TigerDetailVo>  pageQueryMonitorDetails(String handlerGroup,
				String handlerName,long taskId,String bizParam,String ttid, int page,int pageSize);
	
	
}

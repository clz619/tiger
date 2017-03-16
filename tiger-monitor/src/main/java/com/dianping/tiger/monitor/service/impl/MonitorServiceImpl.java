package com.dianping.tiger.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.tiger.monitor.alarm.AlarmManager;
import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.dao.MonitorAlarmDao;
import com.dianping.tiger.monitor.dao.MonitorRecordDao;
import com.dianping.tiger.monitor.dataobject.TigerMonitorAlarmDo;
import com.dianping.tiger.monitor.dataobject.TigerMonitorRecordDo;
import com.dianping.tiger.monitor.es.MonitorDetailEsManager;
import com.dianping.tiger.monitor.service.MonitorService;
import com.dianping.tiger.monitor.vo.TigerDetailVo;


/**
 * monitor record
 * 
 * @author yuantengkai
 */
public class MonitorServiceImpl implements MonitorService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

	@Resource
	private MonitorRecordDao monitorRecordDao;
	
	@Resource
	private MonitorDetailEsManager monitorDetailEsManager;
	
	@Resource
	private MonitorAlarmDao monitorAlarmDao;
	
	@Resource
	private AlarmManager alarmManager;
	
	@Override
	public void dealStatisticsData(String originData) {
		if(StringUtils.isBlank(originData)){
			return;
		}
		TigerMonitorRecordDo record = this.parseOriginData(originData);
		if (record == null) {
			logger.warn("parase originData fail:{}", originData);
			return;
		}
		try{
			monitorRecordDao.addMonitorRecord(record);
		} catch(Exception e){
			logger.error("插入监控统计数据db异常,"+record, e);
		}
	}
	
	/**
	 * 解析接收到的监控统计数据
	 * @param originData
	 * @return
	 */
	private TigerMonitorRecordDo parseOriginData(String originData){
		try {
			// 解析数据：时间戳 | handlerGroup | handlername | hostname | totalNum | sucNum | failNum
			// | avgCost | maxCost | minCost
			String[] strArray = originData.split("\\|");
			if (strArray.length == 10) {
				TigerMonitorRecordDo item = new TigerMonitorRecordDo();
				item.setMonitorTime(new Date(Long.valueOf(strArray[0]
						.trim())));
				item.setHandlerGroup(strArray[1].trim());
				item.setHandlerName(strArray[2].trim());
				item.setHostName(strArray[3].trim());
				item.setTotalNum(Integer.valueOf(strArray[4].trim()));
				item.setSucNum(Integer.valueOf(strArray[5].trim()));
				item.setFailNum(Integer.valueOf(strArray[6].trim()));
				item.setAvgCost(Long.valueOf(strArray[7].trim()));
				item.setMaxCost(Long.valueOf(strArray[8].trim()));
				item.setMinCost(Long.valueOf(strArray[9].trim()));
				return item;
			}
		} catch (Exception e) {
			logger.error("parseOriginData exception,data:" + originData, e);
		}
		return null;
	}
	
	@Override
	public void dealDetailData(String detailData){
		if(StringUtils.isBlank(detailData)){
			return;
		}
		TigerDetailVo detailVo = this.parseDetailData(detailData);
		if(detailVo == null){
			logger.warn("parase detailData fail:{}", detailData);
			return;
		}
		//报警
		if(detailVo.getExecuteResult().indexOf("Retry") != -1 ||
				detailVo.getExecuteResult().indexOf("Fail") != -1){
			alarmManager.put2AlarmDeal(detailVo);
		}
		//建立索引
		try{
			monitorDetailEsManager.buildIndex(detailVo);
		}catch(Exception e){
			logger.error("插入监控详情数据发生异常,"+detailVo, e);
		}
		
	}
	
	private TigerDetailVo parseDetailData(String detailData) {
		try{
			// id|addTime|moitorTime|handlerGroup|handler|node|retryTimes|status
			// |earliestExecuteTime|parameter|host|ttid|bizUniqueId|remark|type
			String[] strArray = detailData.split("\\|");
			if(strArray.length == 15){
				TigerDetailVo detailVo = new TigerDetailVo();
				detailVo.setTaskId(Long.valueOf(strArray[0].trim()));
				detailVo.setAddTime(Long.valueOf(strArray[1].trim()));
				detailVo.setMonitorTime(Long.valueOf(strArray[2].trim()));
				detailVo.setHandlerGroup(strArray[3].trim());
				detailVo.setHandler(strArray[4].trim());
				detailVo.setNode(Integer.valueOf(strArray[5].trim()));
				detailVo.setRetryTimes(Integer.valueOf(strArray[6].trim()));
				detailVo.setStatus(Integer.valueOf(strArray[7].trim()));
				detailVo.setEarliestExecuteTime(Long.valueOf(strArray[8].trim()));
				//parameter json
				String param = strArray[9].trim();
				if(StringUtils.isBlank(param)){
					detailVo.setParameter("");
				}else{
//					JSONObject jb = JSON.parseObject(param);
//					StringBuilder sb = new StringBuilder();
//					for(Entry<String, Object> e : jb.entrySet()){
//						sb.append(e.getKey()).append(" ");
//						sb.append(e.getValue()).append(" ");
//					}
//					detailVo.setParameter(sb.substring(0, sb.length()-1));
					detailVo.setParameter(param);
				}
				detailVo.setHost(strArray[10].trim());
				detailVo.setTtid(strArray[11].trim());
				detailVo.setBizUniqueId(strArray[12].trim());
				detailVo.setRemark(strArray[13].trim());
				detailVo.setExecuteResult(strArray[14].trim());
				return detailVo;
			}
		}catch(Exception e){
			logger.error("parse detailData exception,data:" + detailData, e);
		}
		return null;
	}

	@Override
	public Map<String, List<TigerMonitorRecordDo>> queryMonitorData(String handlerGroup,
			String handlerName, Date monitorTimeFrom, Date monitorTimeTo) {
		// key-hostname
		Map<String, List<TigerMonitorRecordDo>> resultMap = new HashMap<String, List<TigerMonitorRecordDo>>();
		
		if(StringUtils.isBlank(handlerGroup) || StringUtils.isBlank(handlerName)
				|| monitorTimeFrom == null || monitorTimeTo == null){
			return resultMap;
		}
		
		List<TigerMonitorRecordDo> recordList = monitorRecordDao.queryMonitorRecords(handlerGroup, handlerName, monitorTimeFrom, monitorTimeTo);
		if(recordList != null && recordList.size() > 0){
			for(TigerMonitorRecordDo rd : recordList){
				List<TigerMonitorRecordDo> hostRecords = resultMap.get(rd.getHostName());
				if(hostRecords == null){
					hostRecords = new ArrayList<TigerMonitorRecordDo>();
					resultMap.put(rd.getHostName(), hostRecords);
				}
				hostRecords.add(rd);
			}
		}
		return resultMap;
	}


	@Override
	public List<String> queryMonitorHandlers(String handlerGroup, Date monitorTimeFrom, Date monitorTimeTo) {
		List<String> handlerList = new ArrayList<String>();
		if (StringUtils.isBlank(handlerGroup) || monitorTimeFrom == null || monitorTimeTo == null) {
			return handlerList;
		}
		handlerList = monitorRecordDao.queryMonitorHandlers(handlerGroup, monitorTimeFrom, monitorTimeTo);
		return handlerList;
	}
	
	@Override
	public List<String> queryMonitorHandlerGroups(Date monitorTimeFrom,Date monitorTimeTo){
		List<String> groupList = new ArrayList<String>();
		if ( monitorTimeFrom == null || monitorTimeTo == null) {
			return groupList;
		}
		groupList = monitorRecordDao.queryMonitorGroups(monitorTimeFrom, monitorTimeTo);
		return groupList;
	}
	
	@Override
	public PageModel<TigerDetailVo> pageQueryMonitorDetails(String handlerGroup,String handlerName, 
			long taskId, String bizParam,String ttid, int page,int pageSize){
		if(StringUtils.isBlank(handlerGroup)){
			return null;
		}
		if(taskId < 1 && StringUtils.isBlank(handlerName) && StringUtils.isBlank(bizParam) && StringUtils.isBlank(ttid)){
			return null;
		}
		if(page < 1) {
			page = 1;
		}
		if(pageSize > 200) {
			pageSize = 200;
		}
		PageModel<TigerDetailVo> result = monitorDetailEsManager.queryTigerDetails(handlerGroup, handlerName, taskId, ttid, bizParam, page, pageSize);
		return result;
	}
	
	
	@Override
	public long addTigerMonitorAlarm(TigerMonitorAlarmDo monitorAlarm) {
		if(monitorAlarm == null){
			return 0;
		}
		try {
			long id = monitorAlarmDao.addMonitorAlarm(monitorAlarm);
			return id;
		} catch (Exception e) {
			logger.error("monitorAlarmDao.addMonitorAlarm exception," + monitorAlarm, e);
		}
		return 0;
	}

	@Override
	public int updateMonitorAlarmById(long id, int leastFailNum,
			int intervalFailNum, String mailReceives, int offFlag) {
		if(id < 1 || leastFailNum < 1 || intervalFailNum < 1 || StringUtils.isBlank(mailReceives)){
			return 0;
		}
		return monitorAlarmDao.updateAlarmInfo(id, leastFailNum, intervalFailNum, mailReceives,offFlag);
	}
	
	@Override
	public int deleteAlarmById(long id){
		if(id < 1){
			return 0;
		}
		return monitorAlarmDao.deleteAlarmById(id);
	}
	
	@Override
	public TigerMonitorAlarmDo loadAlarmByHandlerGroupAndName(String handlerGroup, String handlerName){
		if(StringUtils.isBlank(handlerGroup) || StringUtils.isBlank(handlerName)){
			return null;
		}
		return monitorAlarmDao.loadAlarmByHandlerGroupAndName(handlerGroup, handlerName);
	}

	@Override
	public PageModel<TigerMonitorAlarmDo> pageQueryMonitorAlarms(
			String handlerGroup, String handlerName, int page, int pageSize) {
		if(StringUtils.isBlank(handlerGroup)){
			return null;
		}
		if(page < 1){
			page = 1;
		}
		return monitorAlarmDao.pageQueryAlarms(page, pageSize, handlerGroup, handlerName);
	}

}
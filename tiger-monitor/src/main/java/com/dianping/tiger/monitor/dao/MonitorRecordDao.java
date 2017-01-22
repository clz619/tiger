/**
 * 
 */
package com.dianping.tiger.monitor.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.dianping.tiger.monitor.dataobject.TigerMonitorRecordDo;

/**
 * @author yuantengkai
 * 监控统计数据dao
 */
public interface MonitorRecordDao {
	
	public long addMonitorRecord(TigerMonitorRecordDo recordDo) throws SQLException;
	
	public List<TigerMonitorRecordDo> queryMonitorRecords(String handlerGroup,
			String handlerName, Date monitorTimeFrom, Date monitorTimeTo);
	
	public List<String> queryMonitorHandlers(String handlerGroup,Date monitorTimeFrom, Date monitorTimeTo);
	
	public List<String> queryMonitorGroups(Date monitorTimeFrom, Date monitorTimeTo);

}

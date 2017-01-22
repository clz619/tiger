/**
 * 
 */
package com.dianping.tiger.monitor.component;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * @author yuantengkai
 * 支持分页的SqlMapClientDaoSupport
 */
public class PageSqlMapClientDaoSupport<T> extends SqlMapClientDaoSupport {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageModel<T> pageQuery(int pageIndex, int pageSize, Map<String,Object> params, 
			String countSqlName, String listSqlName){
		if(StringUtils.isBlank(countSqlName) || StringUtils.isBlank(listSqlName)){
			throw new IllegalArgumentException("countSqlName or listSqlName is null.");
		}
		PageModel<T> page = new PageModel<T>(pageIndex, pageSize);
		//query count
		int count = 0;
		count = (Integer) this.getSqlMapClientTemplate().queryForObject(countSqlName, params);
		page.setRecordCount(count);
		
		//query list
		int start = (pageIndex-1) * pageSize;
		params.put("start", start);
		params.put("limit", pageSize);
		List dataList = this.getSqlMapClientTemplate().queryForList(listSqlName, params);
		page.setRecords(dataList);
		return page;
	}

}

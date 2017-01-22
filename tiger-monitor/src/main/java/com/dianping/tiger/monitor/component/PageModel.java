/**
 * 
 */
package com.dianping.tiger.monitor.component;

import java.util.List;

/**
 * @author yuantengkai
 * 分页对象
 */
public class PageModel<T> {
	
	/**
	 * Total records size
	 */
	private int recordCount;

	/**
	 * The number of records of per page
	 */
	private int pageSize;

	/**
	 * Current page
	 */
	private int currentPage = 1;

	/**
	 * Records
	 */
	private List<T> records;
	
	public PageModel(int currentPage, int pageSize){
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}
	
	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}
	
}

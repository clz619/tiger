/**
 * 
 */
package com.dianping.tiger.monitor.es;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.vo.TigerDetailVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yuantengkai
 *
 */
public class MonitorDetailEsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorDetailEsManager.class);
	
	private static final int TimesOut = 2000;
	
	private static final String indexName = "tigermonitor";
	
	private Client transportClient;
	
	private String clusterName;
	
	//127.0.0.1:9300;192.168.1.1:9300
	private String esServers;
	
	public void init(){
		if(StringUtils.isBlank(clusterName) || StringUtils.isBlank(esServers)){
			throw new IllegalArgumentException("elasticSearch configInfo is null!");
		}
		List<String> serverList = new ArrayList<String>();
		String[] esServerArray = esServers.split(";");
		for(String es:esServerArray){
			serverList.add(es);
		}
		Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        transportClient = TransportClient.builder().settings(settings).build();
        TransportClient tmpClient = (TransportClient) transportClient;
        try {
            for (String esServer : serverList) {
                String[] parts = esServer.split(":");
                InetAddress inetAddress = InetAddress.getByName(parts[0]);
                int port = Integer.parseInt(parts[1]);
                tmpClient.addTransportAddress(new InetSocketTransportAddress(inetAddress,port));
            }
        } catch (Exception e) {
            logger.error("elastic transportClient init exception,clusterName="+clusterName, e);
            throw new RuntimeException("elastic transportClient init exception!");
        }
        logger.warn("elastic transportClient init success.");
	}
	
	public void destroy(){
		if (transportClient != null){
            transportClient.close();
        }
	}
	
	/**
	 * 建立es索引
	 * @param detailVo
	 * @return
	 */
	public Boolean buildIndex(TigerDetailVo detailVo){
		String type = detailVo.getHandlerGroup();
		BulkRequestBuilder bulkBuilder = transportClient.prepareBulk();
		IndexRequestBuilder indexBuilder = transportClient.prepareIndex(indexName, type,"taskid"+detailVo.getTaskId());
		ObjectMapper mapper = new ObjectMapper();
		byte[] jsonByte = null;
        try {
        	jsonByte = mapper.writeValueAsBytes((Object) detailVo);
        	indexBuilder.setSource(jsonByte);
			bulkBuilder.add(indexBuilder);
        } catch (JsonProcessingException e) {
            logger.error("对象转化为json失败,"+detailVo, e);
        }
		
		//发起请求
        BulkResponse bulkResponse = (BulkResponse) bulkBuilder.get();
        if (bulkResponse != null) {
            Iterator<BulkItemResponse> it = bulkResponse.iterator();
            while (it.hasNext()) {
                if(it.next().isFailed()){
                	logger.warn("建立索引失败,"+detailVo);
                	return false;
                }
            }
            return true;
        }
		return false;
	}
	
	/**
	 * 查询监控详情记录
	 * @param type
	 * @param taskId
	 * @param ttid
	 * @param bizParam
	 * @return
	 */
	public PageModel<TigerDetailVo> queryTigerDetails(String type,long taskId, String ttid, String bizParam,int page,int pageSize){
		if(StringUtils.isBlank(type)){
			return null;
		}
		SearchRequestBuilder searchRequest = transportClient.prepareSearch();
		searchRequest.setIndices(indexName);
		searchRequest.setTypes(type);
		int start = (page - 1) * pageSize;
		searchRequest.setFrom(start);
		searchRequest.setSize(pageSize);
		
		//1. bool query
		BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
		if(taskId > 0){
			TermQueryBuilder tqb = new TermQueryBuilder("taskId", taskId);
			boolBuilder.filter(tqb);
		}
		if(!StringUtils.isBlank(ttid)){
			TermQueryBuilder tqb = new TermQueryBuilder("ttid", ttid);
			boolBuilder.filter(tqb);
		}
		if(!StringUtils.isBlank(bizParam)){
			TermQueryBuilder tqb = new TermQueryBuilder("parameter", bizParam);
			boolBuilder.filter(tqb);
		}
		searchRequest.setQuery(boolBuilder);
		
		//2. sort
		FieldSortBuilder fsb = SortBuilders.fieldSort("monitorTime");
		fsb.order(org.elasticsearch.search.sort.SortOrder.ASC);
		searchRequest.addSort(fsb);
		
		//3. search
		SearchResponse response = searchRequest.get(new TimeValue(TimesOut));
		if(response == null) {
			return null;
		}
		PageModel<TigerDetailVo> result = new PageModel<TigerDetailVo>(page,pageSize);
		
		if(response.getHits() != null){
			result.setRecordCount((int) response.getHits().getTotalHits());
			List<TigerDetailVo> detailList = new ArrayList<TigerDetailVo>();
			result.setRecords(detailList);
			for(SearchHit hit : response.getHits().hits()){
				try{
					// hit.getId();
					String tmp = hit.sourceAsString();
					TigerDetailVo record = JSONObject.parseObject(tmp, TigerDetailVo.class);
					detailList.add(record);
				}catch(Exception e){
					logger.error("parse response hit exception,hit:"+hit.sourceAsMap(),e);
				}
            }
		}
		
		return result;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public void setEsServers(String esServers) {
		this.esServers = esServers;
	}

}

/**
 * 
 */
package com.dianping.tiger.biz.task.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;

/**
 * @author yuantengkai
 *
 */
public class MonitorDetailManager {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorDetailManager.class);
	
	public static enum MonitorType {
		Add, Success, Fail, NextRetry, FailRetry, Cancel;
	}
	
	private String monitorUrl;
	
	private String enableMonitor = "false";
	
	private AtomicBoolean queueSendThreadInit = new AtomicBoolean(false);
	
	private BlockingQueue<String> monitorQueue = new LinkedBlockingQueue<String>(50000);;
	
	/**
	 * 监控详情记录
	 * @param taskEntity
	 * @param type
	 */
	public void recordDetail(TigerTaskDo taskEntity,MonitorType type){
		if(!isEnableMonitor()){
			return;
		}
		if(taskEntity == null){
			return;
		}
		// id|addTime|moitorTime|handlerGroup|handler|node|retryTimes|status
		// |earliestExecuteTime|parameter|host|ttid|bizUniqueId|remark|type
		StringBuilder sb = new StringBuilder();
		sb.append(taskEntity.getId()).append("|");
		sb.append(taskEntity.getAddTime().getTime()).append("|");
		sb.append(taskEntity.getUpdateTime().getTime()).append("|");
		sb.append(taskEntity.getHandlerGroup()).append("|");
		sb.append(taskEntity.getHandler()).append("|");
		sb.append(taskEntity.getNode()).append("|");
		sb.append(taskEntity.getRetryTimes()).append("|");
		sb.append(taskEntity.getStatus()).append("|");
		sb.append(taskEntity.getEarliestExecuteTime().getTime()).append("|");
		sb.append(taskEntity.getParameter() == null?"":taskEntity.getParameter()).append("|");
		sb.append(taskEntity.getHost()== null?"":taskEntity.getHost()).append("|");
		sb.append(taskEntity.getTtid()== null?"":taskEntity.getTtid()).append("|");
		sb.append(taskEntity.getBizUniqueId()== null?"":taskEntity.getBizUniqueId()).append("|");
		sb.append(taskEntity.getRemark()== null?"":taskEntity.getRemark()).append("|");
		sb.append(type.name());
		monitorQueue.offer(sb.toString());
		if (queueSendThreadInit.compareAndSet(false, true)) {
			startQueueDeal();
		}
		
	}

	
	private void startQueueDeal() {
		Thread queueThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						String requestParam = monitorQueue.take();
						httpPostSend(requestParam);
					} catch (Exception e) {
						logger.error("Send monitor params happens Exception,", e);
					}
				}
			}

		});
		queueThread.setDaemon(true);
		queueThread.setName("Tiger-Monitor-Thread");
		queueThread.start();
	}
	
	private void httpPostSend(String requestParam) {
		
		String reqURL = monitorUrl +"/tiger/monitorDetail";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(reqURL);
		
		try {
			
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			formParams.add(new BasicNameValuePair("tmd", requestParam));
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).
												setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpPost);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                logger.warn("send http monitorDetail fail,httpcode:"
						+ response.getStatusLine().getStatusCode() + ",reason:"
						+ response.getStatusLine().getReasonPhrase()+",params:"+requestParam);
            } 
            
		} catch (Exception e) {
			logger.error("send http post exception,requrl:" + reqURL
					+ ",params:" + requestParam, e);
		} finally{
            try {
                httpPost.releaseConnection();
                httpClient.close();
            } catch (IOException e) {
                logger.error("httpClient.close IOException", e);
            }
		}
		
	}


	private boolean isEnableMonitor(){
		return "true".equalsIgnoreCase(enableMonitor);
	}
	
	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public void setEnableMonitor(String enableMonitor) {
		this.enableMonitor = enableMonitor;
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("ssss").append("|");
		sb.append("").append("|");
		System.out.println(sb.toString());
	}

}

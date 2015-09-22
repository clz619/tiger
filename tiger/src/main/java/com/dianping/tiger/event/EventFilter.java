/**
 * 
 */
package com.dianping.tiger.event;

import com.dianping.tiger.repository.EventInConsumerRepository;

/**
 * @author yuantengkai
 *
 */
public class EventFilter {
	
	private EventInConsumerRepository eventInConsumerRepository;
	
	public EventFilter(EventInConsumerRepository eventInConsumerRepository){
		this.eventInConsumerRepository = eventInConsumerRepository;
	}
	
	/**
	 * 是否放行
	 * @param taskId
	 * @return
	 */
	public boolean isAccept(Integer taskId){
		return !eventInConsumerRepository.isContain(taskId);
	}

}

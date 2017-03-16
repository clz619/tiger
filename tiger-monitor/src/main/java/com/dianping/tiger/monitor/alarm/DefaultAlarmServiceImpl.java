/**
 * 
 */
package com.dianping.tiger.monitor.alarm;

import javax.annotation.Resource;

import com.dianping.tiger.monitor.alarm.mail.MailEntity;
import com.dianping.tiger.monitor.alarm.mail.MailSender;


/**
 * @author yuantengkai
 *
 */
public class DefaultAlarmServiceImpl implements AlarmService{
		
	@Resource
	private MailSender mailSender;

	@Override
	public void sendMail(String[] toAddr, String subject, String content) {
		if(toAddr == null){
			return;
		}
		MailEntity mail = new MailEntity();
		mail.setToAddress(toAddr);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setContentType("text/plain");
		mailSender.send(mail);
	}

}

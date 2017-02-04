package com.dianping.tiger.monitor.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.dataobject.TigerMonitorRecordDo;
import com.dianping.tiger.monitor.service.MonitorService;
import com.dianping.tiger.monitor.vo.TigerDetailVo;
import com.dianping.tiger.monitor.web.model.ReturnT;

/**
 * monitor 
 * @author yuantengkai
 */
@Controller
@RequestMapping("")
public class MonitorController {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

	private static final SimpleDateFormat FormatDateTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Resource
	private MonitorService monitorService;
	
	@RequestMapping("")
	public String index(){
		return "forward:/tiger";
//		return "redirect:/tiger";
	}

	/**
	 * monitor record index
	 * 
	 * @param model
	 * @param handlerGroup
	 * @param handlerName
	 * @param monitorTime
	 * @return
	 */
	@RequestMapping("/tiger")
	public String tigerIndex(
			Model model,
			String handlerGroup) {
		
		Date now = new Date();
		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.add(Calendar.DAY_OF_MONTH, -7);
		Date startDate = calendarTo.getTime();
		List<String> handlerGroupList = monitorService.queryMonitorHandlerGroups(startDate, now);
		
		model.addAttribute("handlerGroupList", handlerGroupList);
		if(StringUtils.isBlank(handlerGroup)){
			return "record/index";
		}
		
		List<String> handlerNameList = monitorService.queryMonitorHandlers(handlerGroup, startDate, now);
		Collections.sort(handlerNameList);
		model.addAttribute("handlerGroup", handlerGroup);
		model.addAttribute("handlerNameList", handlerNameList);
		
		return "record/index";
	}
	
	/**
	 * monitor detail index
	 * @param model
	 * @param handlerGroup
	 * @return
	 */
	@RequestMapping("/tiger/trace")
	public String tigerTraceIndex(
			Model model,
			String handlerGroup){
		Date now = new Date();
		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.add(Calendar.DAY_OF_MONTH, -7);
		Date startDate = calendarTo.getTime();
		List<String> handlerGroupList = monitorService.queryMonitorHandlerGroups(startDate, now);
		
		model.addAttribute("handlerGroupList", handlerGroupList);
		if(StringUtils.isBlank(handlerGroup)){
			return "detail/index";
		}
		
		List<String> handlerNameList = monitorService.queryMonitorHandlers(handlerGroup, startDate, now);
		Collections.sort(handlerNameList);
		model.addAttribute("handlerGroup", handlerGroup);
		model.addAttribute("handlerNameList", handlerNameList);
		return "detail/index";
	}
	
	@RequestMapping("/tiger/monitorRecord")
	@ResponseBody
	public Map<String, List<TigerMonitorRecordDo>> queryMonitorRecords(
			String handlerGroup,
			String handlerName,
			String monitorTimeFrom,
			String monitorTimeTo) {
		if(StringUtils.isBlank(handlerGroup) || StringUtils.isBlank(handlerName)){
			return null;
		}
		// for param
		Date  monitorTimeFromTime;
		Date  monitorTimeToTime;
		try{
			if(StringUtils.isBlank(monitorTimeFrom)){
				Calendar calendarFrom = Calendar.getInstance();
				calendarFrom.add(Calendar.HOUR_OF_DAY, -2);
				monitorTimeFromTime = calendarFrom.getTime();
			}else{
				monitorTimeFromTime = FormatDateTime.parse(monitorTimeFrom);
			}
			
			if(StringUtils.isBlank(monitorTimeTo)){
				monitorTimeToTime = new Date();
			}else{
				monitorTimeToTime = FormatDateTime.parse(monitorTimeTo);
			}
		}catch(Exception e){
			logger.error("parse monitorTimeFrom or monitorTimeTo error,monitorTimeFrom="
							+monitorTimeFrom+",monitorTimeTo="+monitorTimeTo);
			return null;
		}
		
		Map<String, List<TigerMonitorRecordDo>> map = monitorService
				.queryMonitorData(handlerGroup,handlerName, monitorTimeFromTime,monitorTimeToTime);
		return map;
	}

	@RequestMapping("/tiger/pageListMonitorDetail")
	@ResponseBody
	public Map<String, Object> queryMonitorDetails(String handlerGroup,String handlerName,
			@RequestParam(required=false,defaultValue="0") Long taskId, 
			String bizParam, String ttid,
			int page, int rows){
		if(StringUtils.isBlank(handlerGroup)){
			return null;
		}
		if(taskId == null && StringUtils.isBlank(handlerName) && StringUtils.isBlank(bizParam) && StringUtils.isBlank(ttid)){
			return null;
		}
		if(page < 1){
			page = 0;
		}
		if(rows > 100 || rows <1){
			rows = 20;
		}
		if(taskId == null){
			taskId = 0L;
		}
		if(!StringUtils.isBlank(bizParam)){
			bizParam = bizParam.trim().toLowerCase();
		}
		if(!StringUtils.isBlank(ttid)){
			ttid = ttid.trim(); 
		}
		PageModel<TigerDetailVo> pageResult = monitorService.pageQueryMonitorDetails(handlerGroup,
													handlerName,taskId, bizParam, ttid, page, rows);
		if(pageResult == null){
			return null;
		}
		// result
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", pageResult.getRecordCount());
        resultMap.put("rows", pageResult.getRecords());
        
		return resultMap;
	}
	
	/**
	 * 接收来自tiger应用的监控统计数据
	 * 
	 * @param tm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/tiger/monitor")
	@ResponseBody
	public ReturnT<String> saveMonitorStatistics(String tm)
			throws UnsupportedEncodingException {
		String decodeTm = URLDecoder.decode(tm, "utf-8");
		monitorService.dealStatisticsData(decodeTm);
		return ReturnT.SUCCESS;
	}
	
	/**
	 * 接收来自tiger应用的监控详情数据
	 * @param tmd
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/tiger/monitorDetail")
	@ResponseBody
	public ReturnT<String> saveMonitorDetail(String tmd) throws UnsupportedEncodingException{
		String decodeTmd = URLDecoder.decode(tmd, "utf-8");
		monitorService.dealDetailData(decodeTmd);
		return ReturnT.SUCCESS;
	}
	

}

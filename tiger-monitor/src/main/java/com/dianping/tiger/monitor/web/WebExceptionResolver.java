package com.dianping.tiger.monitor.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dianping.tiger.monitor.web.model.ReturnT;

/**
 * 异常解析器
 * @author yuantengkai
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	
	private static transient Logger logger = LoggerFactory
			.getLogger(WebExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		logger.error("web请求捕获异常WebExceptionResolver,", ex);

		ModelAndView mv = new ModelAndView();
		HandlerMethod method = (HandlerMethod) handler;
		ResponseBody responseBody = method
				.getMethodAnnotation(ResponseBody.class);
		if (responseBody != null) {//说明是ajax请求
			ReturnT<String> result = new ReturnT<String>(500, ex.toString()
					.replaceAll("\n", "<br/>"));
			mv.addObject("result", JSON.toJSONString(result));
			mv.setViewName("common/common.result");
		} else {
			mv.addObject("exceptionMsg", ex.toString()
					.replaceAll("\n", "<br/>"));
			mv.setViewName("common/common.exception");
		}

		return mv;
	}

}

/**
 * 
 */
package com.dianping.tiger.engine.annotation;

/**
 * @author yuantengkai 注解常量类
 */
public interface AnnotationConstants {

	public interface Executor {
		/**
		 * 串行
		 */
		public static final String CHAIN = "chain";
	}
	
	public interface BeanType{
		/**
		 * 单例
		 */
		public static final String SINGLE = "single";
	}

}

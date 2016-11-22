/**
 * 
 */
package com.dianping.tiger.engine.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author yuantengkai
 *
 */
public class ThreadContext {
	
	private static final ThreadLocal<ThreadContext> localContext  = new ThreadLocal<ThreadContext>();

    private Map<String, Object>                     localCacheMap = new HashMap<String, Object>();

    private ThreadContext() {

    }
    /**
     * 开启线程本地存储
     */
    public static void init() {
        ThreadContext ctx = localContext.get();
        if (ctx == null) {
            ctx = new ThreadContext();
            localContext.set(ctx);
        }
    }

    /**
     * 销毁线程本地存储
     */
    public static void destroy() {
        ThreadContext ctx = localContext.get();
        if (ctx != null) {
            localContext.remove();
        }
    }

    public static void put(String key, Object obj) {
        localContext.get()._put(key, obj);
    }

    private void _put(String key, Object obj) {
        localCacheMap.put(key, obj);
    }

    public static Object get(String key) {
    	if(localContext.get() == null){
    		return null;
    	}
        return localContext.get()._get(key);
    }

    private Object _get(String key) {
        return localCacheMap.get(key);
    }

}

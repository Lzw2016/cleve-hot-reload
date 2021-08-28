package org.clever.hot.reload.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/28 14:14 <br/>
 */
public interface CorsProcessor {
    /**
     * 是否支持当前请求跨域
     */
    boolean processRequest(HttpServletRequest request, HttpServletResponse response);
}

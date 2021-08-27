package org.clever.hot.reload.utils;

import org.clever.hot.reload.model.RouteMethod;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/27 22:42 <br/>
 */
public class RouteKeyUtils {
    /**
     * 路由的唯一key
     */
    public static String getRouteKey(String path, RouteMethod routeMethod) {
        return String.format("%s_%s", path, routeMethod);
    }
}

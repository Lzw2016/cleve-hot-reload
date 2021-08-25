package org.clever.hot.reload.model;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/25 15:59 <br/>
 */
@Data
public class RouteInfo implements Serializable {
    /**
     * 路由路径
     */
    private final String path;
    /**
     * 路由method
     */
    private final RouteMethod routeMethod;
    /**
     * class全路径
     */
    private final String clazz;
    /**
     * class method
     */
    private final String method;

    /**
     * @param path        路由路径
     * @param routeMethod 路由method
     * @param clazz       class全路径
     * @param method      class method
     */
    public RouteInfo(String path, RouteMethod routeMethod, String clazz, String method) {
        Assert.hasText(path, "参数path不能为空");
        Assert.notNull(routeMethod, "参数routeMethod不能为null");
        Assert.hasText(clazz, "参数clazz不能为空");
        Assert.hasText(method, "参数method不能为空");
        this.path = path;
        this.routeMethod = routeMethod;
        this.clazz = clazz;
        this.method = method;
    }

    /**
     * 路由的唯一key
     */
    public String getRouteKey() {
        return String.format("%s_%s", path, routeMethod);
    }
}

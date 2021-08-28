package org.clever.hot.reload.spring.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.model.RouteInfo;
import org.clever.hot.reload.route.HttpRoute;
import org.clever.hot.reload.route.HttpRouteRegister;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.utils.ReflectionsUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:31 <br/>
 */
@Slf4j
public class ProductionInterceptorHandler extends AbstractInterceptorHandler {
    protected final ConcurrentMap<RouteInfo, Method> ROUTE_METHOD_MAP = new ConcurrentHashMap<>();

    public ProductionInterceptorHandler(SpringContextHolder springContextHolder, ObjectMapper objectMapper, HotReloadConfig hotReloadConfig) {
        super(springContextHolder, objectMapper, hotReloadConfig);
    }

    @Override
    public void initHttpRoutes() {
        final HttpRouteRegister httpRouteRegister = new HttpRouteRegister();
        final List<String> httpRouteModules = hotReloadConfig.getHttpRouteModules();
        try {
            for (String httpRouteModule : httpRouteModules) {
                Class<?> clazz = Class.forName(httpRouteModule);
                if (HttpRoute.class.isAssignableFrom(clazz)) {
                    HttpRoute httpRoute = (HttpRoute) clazz.newInstance();
                    httpRoute.routing(httpRouteRegister);
                } else {
                    log.error("HttpRoute class={}未实现接口{}", clazz.getName(), HttpRoute.class.getName());
                }
            }
            for (RouteInfo routeInfo : httpRouteRegister.getAllRouteInfo()) {
                Class<?> clazz = Class.forName(routeInfo.getClazz());
                Method method = ReflectionsUtils.getStaticMethod(clazz, routeInfo.getMethod());
                ROUTE_METHOD_MAP.put(routeInfo, method);
            }
        } catch (Exception e) {
            log.error("HttpRoute 加载失败", e);
            System.exit(-1);
        }
        this.httpRouteRegister = httpRouteRegister;
        this.httpRouteRegister.printAllRouteInfo();
    }

    @Override
    public Object doHandle(HttpServletRequest request, HttpServletResponse response, RouteInfo routeInfo) throws Exception {
        Method method = ROUTE_METHOD_MAP.get(routeInfo);
        if (method == null) {
            throw new IllegalArgumentException(String.format("class=%s 未定义 static method=%s", routeInfo.getClazz(), routeInfo.getMethod()));
        }
        return invokeMethod(method);
    }
}

package org.clever.hot.reload.spring.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.model.RouteInfo;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:31 <br/>
 */
@Slf4j
public class ProductionInterceptorHandler extends AbstractInterceptorHandler {
    public ProductionInterceptorHandler(SpringContextHolder springContextHolder, ObjectMapper objectMapper, HotReloadConfig hotReloadConfig) {
        super(springContextHolder, objectMapper, hotReloadConfig);
    }

    @Override
    public void initHttpRoutes() {
    }

    @Override
    public Object doHandle(HttpServletRequest request, HttpServletResponse response, RouteInfo routeInfo) throws Exception {
        return null;
    }
}

package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.HotReloadEngine;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:30 <br/>
 */
@Slf4j
public class HotReloadInterceptorHandler extends AbstractInterceptorHandler {
    private final HotReloadEngine hotReloadEngine;

    public HotReloadInterceptorHandler(SpringContextHolder springContextHolder, HotReloadConfig hotReloadConfig) {
        super(springContextHolder, hotReloadConfig);
        hotReloadEngine = null;
    }

    @Override
    public void initHttpRoutes() {
    }
}

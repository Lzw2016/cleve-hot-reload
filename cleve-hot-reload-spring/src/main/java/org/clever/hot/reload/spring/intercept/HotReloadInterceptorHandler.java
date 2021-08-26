package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.HotReloadEngine;
import org.springframework.context.ApplicationContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:30 <br/>
 */
@Slf4j
public class HotReloadInterceptorHandler extends AbstractInterceptorHandler {
    private final HotReloadEngine hotReloadEngine;

    public HotReloadInterceptorHandler(ApplicationContext applicationContext, HotReloadEngine hotReloadEngine) {
        super(applicationContext);
        this.hotReloadEngine = hotReloadEngine;
    }
}

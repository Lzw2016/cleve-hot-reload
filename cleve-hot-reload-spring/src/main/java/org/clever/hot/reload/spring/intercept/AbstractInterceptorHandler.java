package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:32 <br/>
 */
@Slf4j
public abstract class AbstractInterceptorHandler implements HandlerInterceptor {
    private final SpringContextHolder springContextHolder;
    private final HotReloadConfig hotReloadConfig;

    public AbstractInterceptorHandler(SpringContextHolder springContextHolder, HotReloadConfig hotReloadConfig) {
        Assert.notNull(springContextHolder, "参数springContextHolder不能为null");
        Assert.notNull(hotReloadConfig, "参数hotReloadConfig不能为null");
        this.springContextHolder = springContextHolder;
        this.hotReloadConfig = hotReloadConfig;
    }

    /**
     * 初始化路由信息
     */
    public abstract void initHttpRoutes();

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//    }
}

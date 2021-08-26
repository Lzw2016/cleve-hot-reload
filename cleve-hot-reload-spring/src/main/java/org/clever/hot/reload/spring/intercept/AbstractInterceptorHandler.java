package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:32 <br/>
 */
@Slf4j
public class AbstractInterceptorHandler implements HandlerInterceptor {
    private final ApplicationContext applicationContext;

    public AbstractInterceptorHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

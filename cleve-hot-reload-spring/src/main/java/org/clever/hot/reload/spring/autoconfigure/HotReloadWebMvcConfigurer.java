package org.clever.hot.reload.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.spring.intercept.AbstractInterceptorHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:40 <br/>
 */
@AutoConfigureAfter({HotReloadAutoconfigure.class})
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 5)
@Configuration
@Slf4j
public class HotReloadWebMvcConfigurer implements WebMvcConfigurer {
    private final AbstractInterceptorHandler abstractInterceptorHandler;

    public HotReloadWebMvcConfigurer(AbstractInterceptorHandler abstractInterceptorHandler) {
        this.abstractInterceptorHandler = abstractInterceptorHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(abstractInterceptorHandler).addPathPatterns("/**").order(Integer.MAX_VALUE - 1);
    }
}

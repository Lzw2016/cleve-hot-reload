package org.clever.hot.reload.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.spring.intercept.HotReloadInterceptorHandler;
import org.clever.hot.reload.spring.intercept.ProductionInterceptorHandler;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:18 <br/>
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 4)
@EnableConfigurationProperties({HotReloadConfig.class})
@Configuration
@Slf4j
public class HotReloadAutoconfigure {
    private final HotReloadConfig hotReloadConfig;

    public HotReloadAutoconfigure(HotReloadConfig hotReloadConfig) {
        this.hotReloadConfig = hotReloadConfig;
    }

    @ConditionalOnClass(name = "groovy.util.GroovyScriptEngine")
    @Bean("hotReloadInterceptorHandler")
    public HotReloadInterceptorHandler hotReloadInterceptorHandler() {
        return null;
    }

    @ConditionalOnMissingClass("groovy.util.GroovyScriptEngine")
    @Primary
    @Bean("productionInterceptorHandler")
    public ProductionInterceptorHandler productionInterceptorHandler() {
        return null;
    }
}

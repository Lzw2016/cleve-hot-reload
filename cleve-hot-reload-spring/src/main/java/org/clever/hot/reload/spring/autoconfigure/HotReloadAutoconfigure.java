package org.clever.hot.reload.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.spring.intercept.HotReloadInterceptorHandler;
import org.clever.hot.reload.spring.intercept.ProductionInterceptorHandler;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @ConditionalOnMissingBean
    @Bean("hotReloadSpringContextHolder")
    public SpringContextHolder hotReloadSpringContextHolder() {
        return new SpringContextHolder();
    }

    @ConditionalOnProperty(prefix = HotReloadConfig.CONFIG_ROOT, name = "dev-mode", havingValue = "true", matchIfMissing = true)
    @Bean("hotReloadInterceptorHandler")
    public HotReloadInterceptorHandler hotReloadInterceptorHandler(SpringContextHolder springContextHolder) {
        log.warn("当前已使用代码热重载模式，请勿在生产环境使用这种模式");
        return new HotReloadInterceptorHandler(springContextHolder, hotReloadConfig);
    }

    @ConditionalOnProperty(prefix = HotReloadConfig.CONFIG_ROOT, name = "dev-mode", havingValue = "false")
    @Primary
    @Bean("productionInterceptorHandler")
    public ProductionInterceptorHandler productionInterceptorHandler(SpringContextHolder springContextHolder) {
        return new ProductionInterceptorHandler(springContextHolder, hotReloadConfig);
    }
}

package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:31 <br/>
 */
@Slf4j
public class ProductionInterceptorHandler extends AbstractInterceptorHandler {
    public ProductionInterceptorHandler(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}

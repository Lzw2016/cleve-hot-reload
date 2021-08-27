package org.clever.hot.reload.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:19 <br/>
 */
@ConfigurationProperties(prefix = HotReloadConfig.CONFIG_ROOT)
@Data
public class HotReloadConfig implements Serializable {
    public static final String CONFIG_ROOT = "clever.hot-reload";
    /**
     * 是否启用热重载模式
     */
    private boolean devMode = true;
    /**
     * 热重载代码位置
     */
    private List<String> rootPaths = Collections.singletonList("./src/main/groovy");
    /**
     * http路由模块(org.clever.hot.reload.route.HttpRoute实现类class全路径)
     */
    private List<String> httpRouteModules = new ArrayList<>();
}

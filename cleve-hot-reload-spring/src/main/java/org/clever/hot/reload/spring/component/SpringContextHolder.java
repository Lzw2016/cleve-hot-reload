package org.clever.hot.reload.spring.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * 获取Spring ApplicationContext容器的类<br/>
 * 1.以静态变量保存Spring ApplicationContext<br/>
 * 2.可在任何代码任何地方任何时候取出ApplicaitonContext<br/>
 * 3.提供获取Spring容器中的Bean的方法<br/>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-9 14:25 <br/>
 */
@Slf4j
public class SpringContextHolder implements ApplicationContextAware {
    /**
     * Spring ApplicationContext容器
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("初始化ApplicationContext: {}", applicationContext);
        this.applicationContext = applicationContext;
    }

    /**
     * 获取Spring容器applicationContext对象
     */
    public ApplicationContext getApplicationContext() {
        return getApplicationContext(true);
    }

    /**
     * 获取Spring容器applicationContext对象
     *
     * @param require 是否要求一定要获取到
     */
    @SuppressWarnings("BusyWait")
    public ApplicationContext getApplicationContext(boolean require) {
        while (applicationContext == null && require) {
            log.info("等待Spring Context初始化成功...");
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {
            }
        }
        return applicationContext;
    }

    /**
     * 获取系统根目录
     */
    public String getRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
        } catch (Exception e) {
            log.warn("获取系统根目录失败", e);
        }
        return rootRealPath;
    }

    /**
     * 获取资源根目录
     */
    public String getResourceRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
        } catch (Exception e) {
            log.warn("获取资源根目录失败", e);
        }
        return rootRealPath;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param name Bean名称
     * @return 返回Bean对象
     */
    public <T> T getBean(String name) {
        return getBean(true, name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param require 是否等待获取ApplicationContext
     * @param name    Bean名称
     * @return 返回Bean对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(boolean require, String name) {
        return (T) getApplicationContext(require).getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param requiredType Bean类型
     * @return 返回Bean对象
     */
    public <T> T getBean(Class<T> requiredType) {
        return getBean(true, requiredType);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param require      是否等待获取ApplicationContext
     * @param requiredType Bean类型
     * @return 返回Bean对象
     */
    public <T> T getBean(boolean require, Class<T> requiredType) {
        return getApplicationContext(require).getBean(requiredType);
    }
}

package org.clever.hot.reload;

import groovy.util.GroovyScriptEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 12:30 <br/>
 */
@Slf4j
public class HotReloadEngine {
    private static final String DOT = ".";
    private static final String GROOVY_SUFFIX = ".groovy";
    private static final String JAVA_SUFFIX = ".java";

    private final GroovyScriptEngine engine;

    @SneakyThrows
    public HotReloadEngine(String[] urls) {
        Assert.notNull(urls, "参数urls不能为null");
        Assert.notEmpty(urls, "参数urls不能为空");
        for (String url : urls) {
            String fullPath;
            if (url.contains("://")) {
                fullPath = new URL(url).toString();
            } else {
                File file = new File(url);
                fullPath = file.getAbsolutePath();
            }
            log.info("url={} -> {}", url, fullPath);
        }
        engine = new GroovyScriptEngine(urls);
    }

    public HotReloadEngine(String url) {
        this(new String[]{url});
    }

    /**
     * 动态加载Class
     *
     * @param classFullName class全路径(包点类名称)
     */
    @SneakyThrows
    public Class<?> loadClass(String classFullName) {
        Assert.hasText(classFullName, "参数classFullName不能为空");
        Class<?> clazz;
        String classPath;
        if (StringUtils.endsWithIgnoreCase(classFullName, GROOVY_SUFFIX)) {
            int index = classFullName.length() - GROOVY_SUFFIX.length();
            String path = StringUtils.substring(classFullName, 0, index);
            String suffix = StringUtils.substring(classFullName, index);
            classPath = StringUtils.replace(path, DOT, File.separator) + suffix;
        } else if (StringUtils.endsWithIgnoreCase(classFullName, JAVA_SUFFIX)) {
            int index = classFullName.length() - JAVA_SUFFIX.length();
            String path = StringUtils.substring(classFullName, 0, index);
            String suffix = StringUtils.substring(classFullName, index);
            classPath = StringUtils.replace(path, DOT, File.separator) + suffix;
        } else {
            classPath = StringUtils.replace(classFullName, DOT, File.separator) + GROOVY_SUFFIX;
        }
        clazz = engine.loadScriptByName(classPath);
        return clazz;
    }

    public List<Method> getMethods(String classFullName, String method) {
        Assert.hasText(classFullName, "参数classFullName不能为空");
        Assert.hasText(method, "参数method不能为空");
        Class<?> clazz = loadClass(classFullName);
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods).filter(m -> Objects.equals(method, m.getName())).collect(Collectors.toList());
    }

    public Method getMethod(String classFullName, String method) {
        Assert.hasText(classFullName, "参数classFullName不能为空");
        Assert.hasText(method, "参数method不能为空");
        Method res = null;
        List<Method> methods = getMethods(classFullName, method);
        if (!methods.isEmpty()) {
            res = methods.get(0);
        }
        if (methods.size() > 1) {
            log.warn("class={} 包含{}个 method={}", classFullName, methods.size(), method);
        }
        return res;
    }
}

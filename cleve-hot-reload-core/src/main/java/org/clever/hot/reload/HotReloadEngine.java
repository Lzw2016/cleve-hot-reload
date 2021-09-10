package org.clever.hot.reload;

import groovy.util.GroovyScriptEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.clever.hot.reload.utils.FilePathUtils;
import org.clever.hot.reload.utils.ReflectionsUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
            log.info("url={} -> {}", url, FilenameUtils.normalize(fullPath));
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
        String classPath = FilePathUtils.getClassPath(classFullName);
        clazz = engine.loadScriptByName(classPath);
        // GroovyClassLoader classLoader = new GroovyClassLoader();
        // classLoader.parseClass("SourceCode");
        return clazz;
    }

    public List<Method> getMethods(String classFullName, String methodName) {
        Assert.hasText(classFullName, "参数classFullName不能为空");
        Assert.hasText(methodName, "参数method不能为空");
        Class<?> clazz = loadClass(classFullName);
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods).filter(m -> Objects.equals(methodName, m.getName())).collect(Collectors.toList());
    }

    public Method getMethod(String classFullName, String methodName) {
        Method method = null;
        List<Method> methods = getMethods(classFullName, methodName);
        if (!methods.isEmpty()) {
            method = methods.get(0);
        }
        if (methods.size() > 1) {
            log.warn("class={} 包含{}个 method={}", classFullName, methods.size(), methodName);
        }
        return method;
    }

    @SneakyThrows
    public Object invokeMethod(String classFullName, String methodName, Object... args) {
        Method method = getMethod(classFullName, methodName);
        if (method == null) {
            throw new IllegalArgumentException(String.format("class=%s中不存在static method=%s", classFullName, methodName));
        }
        ReflectionsUtils.makeAccessible(method);
        return method.invoke(null, args);
    }

    public Method getStaticMethod(String classFullName, String methodName) {
        List<Method> methods = getMethods(classFullName, methodName).stream()
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .collect(Collectors.toList());
        Method method = null;
        if (!methods.isEmpty()) {
            method = methods.get(0);
        }
        if (methods.size() > 1) {
            log.warn("class={} 包含{}个 method={}", classFullName, methods.size(), methodName);
        }
        return method;
    }

    @SneakyThrows
    public Object invokeStaticMethod(String classFullName, String methodName, Object... args) {
        Method method = getStaticMethod(classFullName, methodName);
        if (method == null) {
            throw new IllegalArgumentException(String.format("class=%s中不存在static method=%s", classFullName, methodName));
        }
        ReflectionsUtils.makeAccessible(method);
        return method.invoke(null, args);
    }
}

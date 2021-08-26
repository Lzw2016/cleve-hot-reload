package org.clever.hot.reload;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 12:59 <br/>
 */
@Slf4j
public class HotReloadEngineTest {

    @Test
    public void t01() throws InvocationTargetException, IllegalAccessException {
        HotReloadEngine hotReloadEngine = new HotReloadEngine("./src/test/groovy");
        Class<?> clazz_01 = hotReloadEngine.loadClass("org.clever.hot.reload.groovy.Class01");
        log.info("clazz_01 -> {}", clazz_01);
        Class<?> clazz_02 = hotReloadEngine.loadClass("org.clever.hot.reload.groovy.Class01.groovy");
        log.info("clazz_02 -> {}", clazz_02);
        Method method = hotReloadEngine.getMethod("org.clever.hot.reload.groovy.Class01.groovy", "t01");
        method.invoke(null);
        Class<?> clazz_03 = hotReloadEngine.loadClass("org.clever.hot.reload.groovy.Class02.java");
        log.info("clazz_03 -> {}", clazz_03);
        method = hotReloadEngine.getMethod("org.clever.hot.reload.groovy.Class02.java", "t01");
        method.invoke(null);
    }
}

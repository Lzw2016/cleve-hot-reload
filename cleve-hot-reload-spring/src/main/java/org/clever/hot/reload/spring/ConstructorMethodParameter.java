package org.clever.hot.reload.spring;

import org.clever.hot.reload.spring.component.SpringContextHolder;

import java.lang.reflect.Method;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/28 11:46 <br/>
 */
public interface ConstructorMethodParameter {
    /**
     * 方法反射调用时获取方法参数
     */
    Object[] getMethodParameter(SpringContextHolder springContextHolder, Method method);
}

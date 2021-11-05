package org.clever.hot.reload.spring;

import lombok.extern.slf4j.Slf4j;
import org.clever.hot.reload.spring.model.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/28 11:36 <br/>
 */
@Slf4j
public class HotReloadExtendUtils {
    /**
     * 异常处理实现
     */
    public static ExceptionResolver EXCEPTION_RESOLVER = (request, response, handler, e) -> {
        log.warn("[HotReloadHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setError(e.getMessage());
        errorResponse.setMessage("服务器内部错误");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    };

    /**
     * 方法反射调用时获取方法参数
     */
    public static ConstructorMethodParameter CONSTRUCTOR_METHOD_PARAMETER = (springContextHolder, request, response, routeInfo, method) -> {
        final Object[] args = new Object[method.getParameterTypes().length];
        final Class<?>[] parameterTypes = method.getParameterTypes();
        for (int idx = 0; idx < parameterTypes.length; idx++) {
            Class<?> parameterType = parameterTypes[idx];
            if (parameterType.isInstance(request)) {
                args[idx] = request;
            } else if (parameterType.isInstance(response)) {
                args[idx] = response;
            } else if (parameterType.isInstance(routeInfo)) {
                args[idx] = routeInfo;
            } else {
                // TODO 需要优化
                springContextHolder.getBean(parameterType);
            }
        }
        return args;
    };

    /**
     * 跨域支持处理
     */
    public static CorsProcessor CORS_PROCESSOR = null;
}

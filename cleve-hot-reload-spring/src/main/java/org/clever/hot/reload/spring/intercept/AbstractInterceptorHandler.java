package org.clever.hot.reload.spring.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.hot.reload.model.RouteInfo;
import org.clever.hot.reload.model.RouteMethod;
import org.clever.hot.reload.route.HttpRouteRegister;
import org.clever.hot.reload.spring.HotReloadExtendUtils;
import org.clever.hot.reload.spring.component.JacksonMapper;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.utils.ReflectionsUtils;
import org.clever.hot.reload.utils.RouteKeyUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:32 <br/>
 */
@Slf4j
public abstract class AbstractInterceptorHandler implements HandlerInterceptor {
    protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    protected final SpringContextHolder springContextHolder;
    protected final JacksonMapper jacksonMapper;
    protected final HotReloadConfig hotReloadConfig;
    protected volatile HttpRouteRegister httpRouteRegister;

    public AbstractInterceptorHandler(SpringContextHolder springContextHolder, ObjectMapper objectMapper, HotReloadConfig hotReloadConfig) {
        Assert.notNull(springContextHolder, "参数springContextHolder不能为null");
        Assert.notNull(hotReloadConfig, "参数hotReloadConfig不能为null");
        Assert.notNull(objectMapper, "参数objectMapper不能为null");
        Assert.notNull(hotReloadConfig.getRootPaths(), "参数hotReloadConfig.rootPaths不能为null");
        Assert.notNull(hotReloadConfig.getHttpRouteModules(), "参数hotReloadConfig.httpRouteModules不能为null");
        this.springContextHolder = springContextHolder;
        this.jacksonMapper = new JacksonMapper(objectMapper);
        this.hotReloadConfig = hotReloadConfig;
    }

    /**
     * 初始化路由信息
     */
    public abstract void initHttpRoutes();

    /**
     * 判断请求是否支持 Script 处理
     */
    protected boolean supportRequestHandle(HttpServletRequest request, Object handler) {
        // 支持的请求前缀
        boolean support = true;
        // SpringMvc功能冲突处理
        if (handler != null) {
            if (handler instanceof HandlerMethod) {
                log.warn("HotReload Handler被原生SpringMvc功能覆盖 | {}", handler.getClass());
                support = false;
            } else if (handler instanceof ResourceHttpRequestHandler) {
                ResourceHttpRequestHandler resourceHttpRequestHandler = (ResourceHttpRequestHandler) handler;
                Method method = ReflectionUtils.findMethod(ResourceHttpRequestHandler.class, "getResource", HttpServletRequest.class);
                if (method != null) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    Resource resource = (Resource) ReflectionUtils.invokeMethod(method, resourceHttpRequestHandler, request);
                    if (resource != null && resource.exists()) {
                        log.warn("HotReload Handler被静态资源覆盖 | {}", handler.getClass());
                        support = false;
                    }
                }
            } else if (handler instanceof ParameterizableViewController) {
                support = false;
            } else if (!handler.getClass().getName().startsWith("org.springframework.")) {
                log.warn("未知的Handler类型，覆盖Script Handler | {}", handler.getClass());
                support = false;
            }
        }
        return support;
    }

    /**
     * 序列化返回对象
     */
    protected String serializeRes(Object res) {
        return jacksonMapper.toJson(res);
    }

    /**
     * 处理请求
     */
    public abstract Object doHandle(HttpServletRequest request, HttpServletResponse response, RouteInfo routeInfo) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String requestPath = request.getRequestURI();
        final String method = StringUtils.upperCase(request.getMethod());
        final RouteInfo routeInfo = httpRouteRegister.getRouteInfo(RouteKeyUtils.getRouteKey(requestPath, RouteMethod.valueOf(method)));
        if (routeInfo == null) {
            return true;
        }
        if (!supportRequestHandle(request, handler)) {
            return true;
        }
        try {
            Object res = doHandle(request, response, routeInfo);
            if (res != null && !response.isCommitted()) {
                response.setContentType(CONTENT_TYPE);
                String json = serializeRes(res);
                response.getWriter().println(json);
            }
        } catch (Exception e) {
            if (response.isCommitted()) {
                log.info("Script处理请求异常", e);
                return false;
            }
            Object res = HotReloadExtendUtils.EXCEPTION_RESOLVER.resolveException(request, response, handler, e);
            if (res != null) {
                response.setContentType(CONTENT_TYPE);
                String json = serializeRes(res);
                response.getWriter().println(json);
            }
        }
        return false;
    }

    public Object invokeMethod(Method method) throws Exception {
        ReflectionsUtils.makeAccessible(method);
        Object[] args = HotReloadExtendUtils.CONSTRUCTOR_METHOD_PARAMETER.getMethodParameter(springContextHolder, method);
        return method.invoke(null, args);
    }
}

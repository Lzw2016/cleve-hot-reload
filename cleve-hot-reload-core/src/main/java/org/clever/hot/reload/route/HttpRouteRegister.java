package org.clever.hot.reload.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.hot.reload.model.RouteInfo;
import org.clever.hot.reload.model.RouteMethod;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/06/10 21:38 <br/>
 */
@Slf4j
public class HttpRouteRegister {
    private final ConcurrentMap<String, RouteInfo> routeInfoMap = new ConcurrentHashMap<>();

    public RouteInfo getRouteInfo(String routeKey) {
        return routeInfoMap.get(routeKey);
    }

    public void printAllRouteInfo() {
        StringBuilder sb = new StringBuilder();
        List<RouteInfo> routeInfoList = routeInfoMap.values().stream()
                .sorted(Comparator.comparing(RouteInfo::getRouteKey))
                .collect(Collectors.toList());
        final int routeMethodWidth = 9;
        int pathWidth = 0;
        for (RouteInfo routeInfo : routeInfoList) {
            if (routeInfo.getPath().length() > pathWidth) {
                pathWidth = routeInfo.getPath().length();
            }
        }
        for (RouteInfo routeInfo : routeInfoList) {
            sb.append("| ").append(StringUtils.rightPad(String.format("[%s]", routeInfo.getRouteMethod().name()), routeMethodWidth))
                    .append(" ").append(StringUtils.rightPad(routeInfo.getPath(), pathWidth))
                    .append(" -> ").append(routeInfo.getClazz()).append("#").append(routeInfo.getMethod())
                    .append("\n");
        }
        if (sb.length() > 0) {
            String line = "----------------------------------------------------------------------------------------------------------------------------------------------------------";
            log.info("\n{}\n#--All RouteInfo--#\n{}{}", line, sb, line);
        }
    }

    public HttpRouteRegister mapping(String path, RouteMethod routeMethod, String clazz, String method) {
        RouteInfo routeInfo = new RouteInfo(path, routeMethod, clazz, method);
        RouteInfo old = routeInfoMap.get(routeInfo.getRouteKey());
        if (old != null) {
            log.warn("Route被替换 | {} -> {}", old, routeInfo);
        }
        routeInfoMap.put(routeInfo.getRouteKey(), routeInfo);
        return this;
    }

    public HttpRouteRegister get(String path, String clazz, String method) {
        return mapping(path, RouteMethod.GET, clazz, method);
    }

    public HttpRouteRegister post(String path, String clazz, String method) {
        return mapping(path, RouteMethod.POST, clazz, method);
    }

    public HttpRouteRegister put(String path, String clazz, String method) {
        return mapping(path, RouteMethod.PUT, clazz, method);
    }

    public HttpRouteRegister delete(String path, String clazz, String method) {
        return mapping(path, RouteMethod.DELETE, clazz, method);
    }

    public HttpRouteRegister patch(String path, String clazz, String method) {
        return mapping(path, RouteMethod.PATCH, clazz, method);
    }

    public HttpRouteRegister options(String path, String clazz, String method) {
        return mapping(path, RouteMethod.OPTIONS, clazz, method);
    }

    public HttpRouteRegister head(String path, String clazz, String method) {
        return mapping(path, RouteMethod.HEAD, clazz, method);
    }

    public HttpRouteRegister connect(String path, String clazz, String method) {
        return mapping(path, RouteMethod.CONNECT, clazz, method);
    }

    public HttpRouteRegister trace(String path, String clazz, String method) {
        return mapping(path, RouteMethod.TRACE, clazz, method);
    }

    public ClassRegisterToRoute startClass(String clazz, String basePath) {
        return new ClassRegisterToRoute(this, basePath, clazz);
    }

    public ClassRegisterToRoute startClass(String clazz) {
        return startClass(StringUtils.EMPTY, clazz);
    }

    public BasePathRegisterToRoute startBasePath(String basePath) {
        return new BasePathRegisterToRoute(this, basePath);
    }

    public static class ClassRegisterToRoute {
        private final HttpRouteRegister register;
        private final String basePath;
        private final String clazz;

        public ClassRegisterToRoute(HttpRouteRegister register, String basePath, String clazz) {
            Assert.notNull(register, "参数register不能为null");
            Assert.notNull(basePath, "参数basePath不能为null");
            Assert.hasText(clazz, "参数clazz不能为空");
            this.register = register;
            this.basePath = basePath;
            this.clazz = clazz;
        }

        public HttpRouteRegister endClass() {
            return register;
        }

        public ClassRegisterToRoute mapping(String path, RouteMethod routeMethod, String method) {
            register.mapping(basePath + path, routeMethod, clazz, method);
            return this;
        }

        public ClassRegisterToRoute get(String path, String method) {
            return mapping(path, RouteMethod.GET, method);
        }

        public ClassRegisterToRoute post(String path, String method) {
            return mapping(path, RouteMethod.POST, method);
        }

        public ClassRegisterToRoute put(String path, String method) {
            return mapping(path, RouteMethod.PUT, method);
        }

        public ClassRegisterToRoute delete(String path, String method) {
            return mapping(path, RouteMethod.DELETE, method);
        }

        public ClassRegisterToRoute patch(String path, String method) {
            return mapping(path, RouteMethod.PATCH, method);
        }

        public ClassRegisterToRoute options(String path, String method) {
            return mapping(path, RouteMethod.OPTIONS, method);
        }

        public ClassRegisterToRoute head(String path, String method) {
            return mapping(path, RouteMethod.HEAD, method);
        }

        public ClassRegisterToRoute connect(String path, String method) {
            return mapping(path, RouteMethod.CONNECT, method);
        }

        public ClassRegisterToRoute trace(String path, String method) {
            return mapping(path, RouteMethod.TRACE, method);
        }

        public ClassRegisterToRoute defMapping(RouteMethod routeMethod, String method) {
            return mapping(basePath, routeMethod, method);
        }

        public ClassRegisterToRoute defGet(String method) {
            return mapping(basePath, RouteMethod.GET, method);
        }

        public ClassRegisterToRoute defPost(String method) {
            return mapping(basePath, RouteMethod.POST, method);
        }

        public ClassRegisterToRoute defPut(String method) {
            return mapping(basePath, RouteMethod.PUT, method);
        }

        public ClassRegisterToRoute defDelete(String method) {
            return mapping(basePath, RouteMethod.DELETE, method);
        }

        public ClassRegisterToRoute defPatch(String method) {
            return mapping(basePath, RouteMethod.PATCH, method);
        }

        public ClassRegisterToRoute defOptions(String method) {
            return mapping(basePath, RouteMethod.OPTIONS, method);
        }

        public ClassRegisterToRoute defHead(String method) {
            return mapping(basePath, RouteMethod.HEAD, method);
        }

        public ClassRegisterToRoute defConnect(String method) {
            return mapping(basePath, RouteMethod.CONNECT, method);
        }

        public ClassRegisterToRoute defTrace(String method) {
            return mapping(basePath, RouteMethod.TRACE, method);
        }
    }

    public static class BasePathRegisterToRoute {
        private final HttpRouteRegister register;
        private final String basePath;

        public BasePathRegisterToRoute(HttpRouteRegister register, String basePath) {
            Assert.notNull(register, "参数register不能为null");
            Assert.notNull(basePath, "参数basePath不能为null");
            this.register = register;
            this.basePath = basePath;
        }

        public HttpRouteRegister endBasePath() {
            return register;
        }

        public BasePathRegisterToRoute mapping(String path, RouteMethod routeMethod, String clazz, String method) {
            register.mapping(basePath + path, routeMethod, clazz, method);
            return this;
        }

        public BasePathRegisterToRoute get(String path, String clazz, String method) {
            return mapping(path, RouteMethod.GET, clazz, method);
        }

        public BasePathRegisterToRoute post(String path, String clazz, String method) {
            return mapping(path, RouteMethod.POST, clazz, method);
        }

        public BasePathRegisterToRoute put(String path, String clazz, String method) {
            return mapping(path, RouteMethod.PUT, clazz, method);
        }

        public BasePathRegisterToRoute delete(String path, String clazz, String method) {
            return mapping(path, RouteMethod.DELETE, clazz, method);
        }

        public BasePathRegisterToRoute patch(String path, String clazz, String method) {
            return mapping(path, RouteMethod.PATCH, clazz, method);
        }

        public BasePathRegisterToRoute options(String path, String clazz, String method) {
            return mapping(path, RouteMethod.OPTIONS, clazz, method);
        }

        public BasePathRegisterToRoute head(String path, String clazz, String method) {
            return mapping(path, RouteMethod.HEAD, clazz, method);
        }

        public BasePathRegisterToRoute connect(String path, String clazz, String method) {
            return mapping(path, RouteMethod.CONNECT, clazz, method);
        }

        public BasePathRegisterToRoute trace(String path, String clazz, String method) {
            return mapping(path, RouteMethod.TRACE, clazz, method);
        }

        public BasePathRegisterToRoute defMapping(RouteMethod routeMethod, String clazz, String method) {
            return mapping(basePath, routeMethod, clazz, method);
        }

        public BasePathRegisterToRoute defGet(String clazz, String method) {
            return mapping(basePath, RouteMethod.GET, clazz, method);
        }

        public BasePathRegisterToRoute defPost(String clazz, String method) {
            return mapping(basePath, RouteMethod.POST, clazz, method);
        }

        public BasePathRegisterToRoute defPut(String clazz, String method) {
            return mapping(basePath, RouteMethod.PUT, clazz, method);
        }

        public BasePathRegisterToRoute defDelete(String clazz, String method) {
            return mapping(basePath, RouteMethod.DELETE, clazz, method);
        }

        public BasePathRegisterToRoute defPatch(String clazz, String method) {
            return mapping(basePath, RouteMethod.PATCH, clazz, method);
        }

        public BasePathRegisterToRoute defOptions(String clazz, String method) {
            return mapping(basePath, RouteMethod.OPTIONS, clazz, method);
        }

        public BasePathRegisterToRoute defHead(String clazz, String method) {
            return mapping(basePath, RouteMethod.HEAD, clazz, method);
        }

        public BasePathRegisterToRoute defConnect(String clazz, String method) {
            return mapping(basePath, RouteMethod.CONNECT, clazz, method);
        }

        public BasePathRegisterToRoute defTrace(String clazz, String method) {
            return mapping(basePath, RouteMethod.TRACE, clazz, method);
        }
    }
}

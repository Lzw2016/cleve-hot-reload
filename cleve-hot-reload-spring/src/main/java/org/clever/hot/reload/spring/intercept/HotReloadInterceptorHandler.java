package org.clever.hot.reload.spring.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.hot.reload.HotReloadEngine;
import org.clever.hot.reload.model.RouteInfo;
import org.clever.hot.reload.route.HttpRoute;
import org.clever.hot.reload.route.HttpRouteRegister;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.spring.watch.FileSystemWatcher;
import org.clever.hot.reload.utils.FilePathUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:30 <br/>
 */
@Slf4j
public class HotReloadInterceptorHandler extends AbstractInterceptorHandler {
    protected final HotReloadEngine hotReloadEngine;

    public HotReloadInterceptorHandler(SpringContextHolder springContextHolder, ObjectMapper objectMapper, HotReloadConfig hotReloadConfig) {
        super(springContextHolder, objectMapper, hotReloadConfig);
        hotReloadEngine = new HotReloadEngine(hotReloadConfig.getRootPaths().toArray(new String[0]));
        watchHttpRouteModules();
    }

    public void watchHttpRouteModules() {
        final List<String> rootPaths = hotReloadConfig.getRootPaths();
        final List<String> httpRouteModules = hotReloadConfig.getHttpRouteModules();
        final List<String> absolutePaths = new ArrayList<>();
        final Set<String> includes = new HashSet<>();
        for (String rootPath : rootPaths) {
            File file = new File(rootPath);
            if (file.isDirectory()) {
                absolutePaths.add(FilenameUtils.normalize(file.getAbsolutePath()));
            }
        }
        for (String httpRouteModule : httpRouteModules) {
            final String classPath = FilePathUtils.getClassPath(httpRouteModule);
            for (String absolutePath : absolutePaths) {
                final String include = FilenameUtils.concat(absolutePath, classPath);
                includes.add(FilenameUtils.normalize(include));
            }
        }
        if (includes.isEmpty()) {
            return;
        }
        log.info("\n监听HttpRouteModules文件: \n{}", StringUtils.join(includes, "\n"));
        List<FileSystemWatcher> fileSystemWatcherList = new ArrayList<>(absolutePaths.size());
        for (String absolutePath : absolutePaths) {
            FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(
                    absolutePath,
                    includes,
                    event -> {
                        log.info("HttpRoute变化 | [{}] -> [{}]", event.getEventType(), event.getFileOrDir().getAbsolutePath());
                        this.initHttpRoutes();
                    },
                    3_000,
                    300
            );
            fileSystemWatcherList.add(fileSystemWatcher);
        }
        for (FileSystemWatcher fileSystemWatcher : fileSystemWatcherList) {
            fileSystemWatcher.start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> fileSystemWatcherList.forEach(FileSystemWatcher::stop)));
    }

    @Override
    public void initHttpRoutes() {
        final HttpRouteRegister httpRouteRegister = new HttpRouteRegister();
        final List<String> httpRouteModules = hotReloadConfig.getHttpRouteModules();
        for (String httpRouteModule : httpRouteModules) {
            try {
                Class<?> clazz = hotReloadEngine.loadClass(httpRouteModule);
                if (clazz != null && HttpRoute.class.isAssignableFrom(clazz)) {
                    try {
                        HttpRoute httpRoute = (HttpRoute) clazz.newInstance();
                        httpRoute.routing(httpRouteRegister);
                    } catch (Exception e) {
                        log.error("HttpRoute class={}执行routing失败", httpRouteModule, e);
                    }
                } else if (clazz == null) {
                    log.error("HttpRoute httpRouteModule={}未定义class", httpRouteModule);
                } else {
                    log.error("HttpRoute class={}未实现接口{}", clazz.getName(), HttpRoute.class.getName());
                }
            } catch (Exception e) {
                log.error("HttpRoute class={}加载失败", httpRouteModule, e);
            }
        }
        this.httpRouteRegister = httpRouteRegister;
        this.httpRouteRegister.printAllRouteInfo();
    }

    @Override
    public Object doHandle(HttpServletRequest request, HttpServletResponse response, RouteInfo routeInfo) throws Exception {
        final Method method = hotReloadEngine.getStaticMethod(routeInfo.getClazz(), routeInfo.getMethod());
        if (method == null) {
            throw new IllegalArgumentException(String.format("class=%s 未定义 static method=%s", routeInfo.getClazz(), routeInfo.getMethod()));
        }
        return invokeMethod(method);
    }
}

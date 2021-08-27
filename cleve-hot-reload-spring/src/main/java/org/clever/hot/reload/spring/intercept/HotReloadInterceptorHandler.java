package org.clever.hot.reload.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.clever.hot.reload.HotReloadEngine;
import org.clever.hot.reload.route.HttpRoute;
import org.clever.hot.reload.route.HttpRouteRegister;
import org.clever.hot.reload.spring.component.SpringContextHolder;
import org.clever.hot.reload.spring.config.HotReloadConfig;
import org.clever.hot.reload.spring.watch.FileSystemWatcher;
import org.clever.hot.reload.utils.FilePathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/26 23:30 <br/>
 */
@Slf4j
public class HotReloadInterceptorHandler extends AbstractInterceptorHandler {
    private final HotReloadEngine hotReloadEngine;

    public HotReloadInterceptorHandler(SpringContextHolder springContextHolder, HotReloadConfig hotReloadConfig) {
        super(springContextHolder, hotReloadConfig);
        hotReloadEngine = null;
        watchHttpRouteModules();
    }

    public void watchHttpRouteModules() {
        final List<String> rootPaths = hotReloadConfig.getRootPaths();
        final List<String> httpRouteModules = hotReloadConfig.getHttpRouteModules();
        final List<String> absolutePaths = new ArrayList<>();
        final List<String> includes = new ArrayList<>();
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
        List<FileSystemWatcher> fileSystemWatcherList = new ArrayList<>(absolutePaths.size());
        for (String absolutePath : absolutePaths) {
            FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(
                    absolutePath,
                    includes.toArray(new String[0]),
                    null,
                    IOCase.SYSTEM,
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
                if (clazz.isAssignableFrom(HttpRoute.class)) {
                    try {
                        HttpRoute httpRoute = (HttpRoute) clazz.newInstance();
                        httpRoute.routing(httpRouteRegister);
                    } catch (Exception e) {
                        log.error("HttpRoute class={}执行routing失败", httpRouteModule, e);
                    }
                } else {
                    log.error("");
                }
            } catch (Exception e) {
                log.error("HttpRoute class={}加载失败", httpRouteModule, e);
            }
        }
        this.httpRouteRegister = httpRouteRegister;
    }
}

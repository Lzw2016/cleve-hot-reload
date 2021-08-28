package org.clever.hot.reload.spring.demo

import org.clever.hot.reload.route.HttpRoute
import org.clever.hot.reload.route.HttpRouteRegister

class HttpRouteDemo implements HttpRoute {

    @Override
    void routing(HttpRouteRegister register) {
        register.startClass("org.clever.hot.reload.spring.demo.controller.Test01", "/api/test")
                .get("/t01", "t01")
                .get("/t02", "t02")
                .endClass()
    }
}

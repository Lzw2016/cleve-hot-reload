package org.clever.hot.reload.spring.demo.controller

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletRequest

@Slf4j
class Test01 {
    static Object t01() {
        return [a: 111, b: "abc", ok: true]
    }

    static Object t02(HttpServletRequest request) {
        log.info("-> {}", request.getRequestURI())
        // throw new RuntimeException("AAA")
        return [
                now: System.currentTimeMillis(),
                aaa: 123,
        ]
    }
}

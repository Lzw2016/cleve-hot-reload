package org.clever.hot.reload.spring.demo.controller

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletRequest

@Slf4j
class Test01 {
    static Object t01() {
        def sum = 0L
        for (def i = 0; i < 10000; i++) {

            for (def j = 0; j < 10000; j++) {
                sum += i + j;
            }
        }
        return [a: 111, b: "abc", ok: true, sum: sum]
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

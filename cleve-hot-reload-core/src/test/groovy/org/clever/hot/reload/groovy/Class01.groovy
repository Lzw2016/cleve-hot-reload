package org.clever.hot.reload.groovy

import groovy.util.logging.Slf4j

@Slf4j
class Class01 {
    static def t01() {
        log.info("Class01#t01 -> 测试")
    }

    static def t02(def a, def b) {
        log.info("Class01#t02 -> a={} | b={}", a, b)
    }

    static def t03(int a, int b) {
        return a + b;
    }
}

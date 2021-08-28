package org.clever.hot.reload.spring.demo.controller

class Test01 {
    static Object t01() {
        return [a: 111, b: "abc", ok: true]
    }

    static Object t02() {
        // throw new RuntimeException("AAA")
        return [
                now: System.currentTimeMillis()
        ]
    }
}

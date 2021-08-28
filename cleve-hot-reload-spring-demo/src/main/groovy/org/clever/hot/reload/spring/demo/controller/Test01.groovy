package org.clever.hot.reload.spring.demo.controller

class Test01 {
    static Object t01() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 123);
        map.put("b", "abc");
        map.put("ok", true);
        return map;
    }
}

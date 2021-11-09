package grammar

import groovy.transform.*
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

@MapConstructor
@TupleConstructor
@EqualsAndHashCode
@ToString
class EntityA {
    String a
    Integer b
    Double c
    BigDecimal d
    Boolean e
}

@Canonical
class EntityB {
    String a
    Integer b
    Double c
    BigDecimal d
    Boolean e
    List<String> f
}

@Canonical
class EntityMixin {
    String a
    Integer b
    Double c
    BigDecimal d
    Boolean e
    List<Long> f
    EntityA g
    EntityB h
    List<EntityA> i
    Set<EntityB> j
}

@Slf4j
class tmp {
    static int add(int x, int y, int z) {
        return x + y + z
    }

    @Test
    void t01() {
        Object tmp = null
        // 单引号和三单引号不支持插入变量, 其他均支持插入变量
        String str_1 = 'aaa' + 'bbb'
        String str_2 = '''aaa bbb'''
        String str_3 = "aaa" + "bbb"
        String str_4 = """aaa bbb"""
        String str_5 = /aaa aaa/
        String str_6 = $/aaa bbb/$
        String str_7 = """aaa ${str_1}"""
        String str_8 = """aaa $str_1"""
        String str_9 = """aaa ${-> str_1}"""

        int int_1 = 0b1010
        int int_2 = 010
        int int_3 = 0x10
        log.info("-> {}", 3.intdiv(2))

        def list_1 = [1, 2, 3, 4, 5]
        def list_2 = [1, "2", new Date()] as LinkedList<Serializable>

        def map_1 = [a: 1, b: 2]
        def map_2 = [a: 1, b: true]
        map_1.a = 3
        map_2["c"] = new Date()

        // ** | ?: | ?. | .& | ~ | *
        String value_0 = null
        def value_1 = 2**8
        def value_2 = value_0 ?: str_1
        def value_3 = str_1 ?: str_2
        def value_4 = value_0?.getBytes()

        def value_5 = str_1.&toUpperCase
        def value_6 = value_5()

        def value_7 = ~".*foo.*"
        def value_8 = value_7.matcher("aaa_foo_bbb").matches()

        def value_9 = [1, 2, 3]
        tmp = add(*value_9)
        def value_10 = [1, 3]
        tmp = add(2, *value_10)
        def value_11 = [1, 2, *value_9, 6]
        def value_12 = [c: 3, d: 4]
        def value_13 = [a: 1, b: 2, *: value_12]

        // .. 等价于 []; ..< 等价于 [)
        def value_14 = 1..10
        def value_15 = (1..10).collect()
        def value_16 = ('a'..<'g').collect()

        // <=> 相当于compareTo方法
        def value_17 = 3 <=> 8

        // == 相当于Java中的 equals()
        def value_18 = new String("a") == new String("a")
        // Groovy(3+) === 或者 is() 相当于Java中的 ==

        def value_19 = { int x, int y, int z ->
            return x + y + z
        }
        tmp = value_19(1, 2, 3)

        log.info("-> end")
    }

    @Test
    void t02() {
        EntityA value_1 = new EntityA()
        value_1.a = "12345"
        log.info("-> {}", value_1)

        EntityA value_2 = new EntityA(a: 1, b: 123, c: 123.456, d: 456.123, e: true)
        log.info("-> {}", value_2)

        def map_1 = [a: 1, b: 123, c: 123.456, d: 456.123, e: true]
        EntityA value_3 = new EntityA(map_1)
        log.info("-> {}", value_3)
        // noinspection GroovyAccessibility .@ 直接获取对象属性
        log.info("-> {}", value_3.@a)

        def value_4 = [value_1, value_2, value_3]
        log.info("-> {}", value_4*.a)
        value_4 = null
        log.info("-> {}", value_4*.a)

        map_1 = [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]]
        EntityB value_5 = new EntityB(map_1)
        log.info("-> {}", value_5)

        map_1 = [
                a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"],
                g: [a: 1, b: 123, c: 123.456, d: 456.123, e: true],
                h: [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                i: [
                        [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                        [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                        [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                ],
                j: [
                        [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                        [a: 1, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                        [a: 2, b: 123, c: 123.456, d: 456.123, e: true, f: ["aaa", "bbb", "ccc", "ddd"]],
                ],
        ]
        EntityMixin value_6 = new EntityMixin(map_1)
        log.info("-> {}", value_6)

        log.info("-> end")
    }
}



























































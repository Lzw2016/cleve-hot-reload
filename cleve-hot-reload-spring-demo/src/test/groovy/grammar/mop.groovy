package grammar

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Test

@Slf4j
class Man {
    void talk() {
        log.info "talk: hello"

    }

    static void eat() {
        log.info "eat: fish"
    }
}


@Slf4j
class mop {

    mop() {
        Man.metaClass.invokeMethod = { String name, args ->
            log.info "--> start call method: $name"
            def targetMethod = Man.metaClass.getMetaMethod(name, args)
            targetMethod.invoke(delegate, args)
            log.info "<-- end call method: $name"
        }
    }

    @Test
    void t01() {
        def man = new Man()
        man.talk()
        log.info "-----------------------------------------------------------------------------------------------"
        man.eat()
        log.info "-----------------------------------------------------------------------------------------------"
        Man.eat()
    }

    @Test
    void t02() {
        // def eat = Man.class.metaClass.getMetaMethod("eat", null)
        def eat = Man.class.metaClass.getStaticMetaMethod("eat", null)
        Man.metaClass.'static'.eat = { ->
            log.info "--> start call eat"
            eat.invoke(null, null)
            log.info "<-- end call eat"
        }
        Man.eat()
    }

    @Test
    void t03() {
        def flag = false
        Man.metaClass.'static'.eat = { ->
            if (flag) {
                return
            }
            flag = true
            log.info "--> start call eat"
            Man.class.metaClass.invokeMethod(null, "eat", null)
            log.info "<-- end call eat"
            flag = false
        }
        Man.eat()
    }

    @Test
    void t04() {
        def flag = false
        Man.metaClass.'static'.eat = { ->
            if (flag) {
                return
            }
            flag = true
            log.info "--> start call eat"
            Man.class.getMethod("eat").invoke(null, null)
            log.info "<-- end call eat"
            flag = false
        }
        Man.eat()
    }

    @Test
    void t05() {
        Man.class.metaClass.invokeMethod(null, "eat", null)
    }
}

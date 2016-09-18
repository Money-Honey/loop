import spock.lang.Specification


/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
class GroovySpec extends Specification {

    def "first test"() {
        given:
            def a = 1
            sleep(10000)

        when:
            a += 1

        then:
            a == 2
    }
}


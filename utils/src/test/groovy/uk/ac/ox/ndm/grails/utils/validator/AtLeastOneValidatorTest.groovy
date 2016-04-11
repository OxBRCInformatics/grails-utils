package uk.ac.ox.ndm.grails.utils.validator

import grails.util.Pair
import spock.lang.Specification

/**
 * @since 24/11/2015
 */
class AtLeastOneValidatorTest extends Specification {

    AtLeastOneValidator validator
    Pair<Object, Object[]> test

    def setup() {
        validator = new AtLeastOneValidator('test')
    }

    def "value with empty list is valid"() {

        test = new Pair("test", [] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "value with object which is null in list is valid"() {
        def empty = null
        Object[] objs = [empty] as Object[]
        test = new Pair("test", objs)

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "value with objects which are null in list is valid"() {
        def empty = null
        def empty2 = null

        test = new Pair("test", [empty, empty2] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "no value with objects which is something in list is valid"() {

        test = new Pair(null, ["something"] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "no value with 1 object which is something in list is valid"() {

        test = new Pair(null, ["something", null] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }


    def "value with object which is something in list is valid"() {

        test = new Pair("test", ["something"] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "value with objects which is something in list is valid"() {

        test = new Pair("test", ["something", "more"] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "value with other objects which is something in list is valid"() {

        test = new Pair("test", ["something", "more", 1] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "no value with other objects which is something in list is valid"() {

        test = new Pair(null, ["something", "more", 1, null] as Object[])

        expect:
        assert validator.isValid(test) instanceof Boolean
    }

    def "no value with other object using java 7 notation which is something in list is valid"() {

        expect:
        validator.isValid(null, "something", "more", 1, null)
    }

    def "no value with objects which are null in list is invalid"() {
        def empty = null
        def empty2 = null

        test = new Pair(null, [empty, empty2] as Object[])

        expect:
        validator.isValid(test)[0] == "validation.choice.atleastone"
    }

    def "no value with no objects in list is invalid"() {
        def empty = null
        def empty2 = null

        test = new Pair(null, [] as Object[])

        expect:
        validator.isValid(test)[0] == "validation.choice.atleastone"
    }
}

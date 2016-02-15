package uk.ac.ox.ndm.utils

import spock.lang.Specification

/**
 * @since 16/09/2015
 */
class UtilsTest extends Specification {

    String[] mappings
    TestUtils utils = new TestUtils()

    void "test empty map breaking"() {
        when: 'mappings is empty'
        mappings = [] as String[]

        then: 'empty map returned'
        utils.extractNameMappings(mappings).isEmpty()

        when: 'mappings is null'
        mappings = null

        then: 'empty map returned'
        utils.extractNameMappings(mappings).isEmpty()
    }

    void "test single string entry with invalid entry"() {
        when: 'mappings is invalid as sub map on value'
        mappings = ['a:1.more'] as String[]
        utils.extractNameMappings(mappings)

        then: 'Exception is thrown'
        IllegalStateException e = thrown()
        e.message == 'Cannot have key:value and contents'

        when: 'mappings is valid as already defined map'
        mappings = ['a.b:1', 'a:1'] as String[]
        utils.extractNameMappings(mappings)

        then: 'Exception is thrown'
        notThrown(IllegalStateException)

        when: 'mappings is valid as already defined value'
        mappings = ['a:1', 'a.b:1'] as String[]
        utils.extractNameMappings(mappings)

        then: 'Exception is thrown'
        notThrown(IllegalStateException)
    }

    void "test single string entry with 1 level"() {

        when: 'mappings is test:map_entry'
        mappings = ['test:map_entry'] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned'
        !result.isEmpty()

        and: 'it has one entry with key "test" and value "map_entry"'
        result.test == 'map_entry'
    }

    void "test multiple string entries with 1 level"() {

        when: 'mappings is a larger array but all elements are only 1 level deep'
        mappings = ['a:1', "b:2", "c:3"] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 3

        and: 'each entry is as expected'
        result.a == '1'
        result.b == '2'
        result.c == '3'
    }

    void "test single string entries with 2 levels"() {

        when: 'mappings is a single entry with 2 levels'
        mappings = ['a.b:1'] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 1'
        !result.isEmpty()
        result.size() == 1

        and: 'each entry is as expected'
        result.a.b == '1'
    }

    void "test multiple string entries with 2 levels"() {

        when: 'mappings is a larger array and all elements are only 2 level deep'
        mappings = ['a.d:1', "b.e:2", "c.f:3"] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 3

        and: 'each entry is as expected'
        result.a.d == '1'
        result.b.e == '2'
        result.c.f == '3'
    }

    void "test single string entry with 3 levels"() {

        when: 'mappings is a single entry with 2 levels'
        mappings = ['a.b.c:1'] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 1'
        !result.isEmpty()
        result.size() == 1

        and: 'each entry is as expected'
        result.a.b.c == '1'
    }

    void "test multiple string entries with 3 levels"() {

        when: 'mappings is a larger array and all elements are only 3 level deep'
        mappings = ['a.d.g:1', "b.e.h:2", "c.f.i:3"] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 3

        and: 'each entry is as expected'
        result.a.d.g == '1'
        result.b.e.h == '2'
        result.c.f.i == '3'
    }

    void "test multiple string entries with mixed levels but no matching sub elements"() {

        when: 'mappings is a complex array'
        mappings = ['a.d.g:1', "b.e:2", "c:3"] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 3

        and: 'each entry is as expected'
        result.a.d.g == '1'
        result.b.e == '2'
        result.c == '3'
    }

    void "test multiple string entries with mixed levels and matching sub elements"() {

        when: 'mappings is a complex array'
        mappings = ['a.d.g:1', "b.e:2", "c:3", 'a.d.f:4', 'b.h:5'] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 3

        and: 'each entry is as expected'
        result.a.d.g == '1'
        result.a.d.f == '4'
        result.b.e == '2'
        result.b.h == '5'
        result.c == '3'
    }

    void "test multiple string entries with matching sub elements and a subelement being a mapping"() {

        when: 'mappings is a complex array'
        mappings = ['a.b:1', "a:2"] as String[]
        def result = utils.extractNameMappings(mappings)

        then: 'non empty map is returned with size 3'
        !result.isEmpty()
        result.size() == 1

        and: 'each entry is as expected'
        result.a.b == '1'
        result.a.'-' == '2'

    }


    class TestUtils implements Utils {

    }
}

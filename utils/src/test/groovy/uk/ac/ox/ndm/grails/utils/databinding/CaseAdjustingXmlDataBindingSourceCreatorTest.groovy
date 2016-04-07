package uk.ac.ox.ndm.grails.utils.databinding

import spock.lang.Shared
import spock.lang.Specification

/**
 * @since 02/02/2016
 */
class CaseAdjustingXmlDataBindingSourceCreatorTest extends Specification {

    @Shared
    CaseAdjustingXmlDataBindingSourceCreator creator

    void setup() {
        creator = new CaseAdjustingXmlDataBindingSourceCreator()
        creator.initialised = true
    }

    def 'test preprocessing of value map keys'() {

        when: 'empty map chosen'
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap([:])

        then: 'no exception is thrown'
        noExceptionThrown()

        when: 'null map chosen'
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(null)

        then: 'exception thrown'
        thrown(IllegalArgumentException)

        expect: 'simple map with hypenated keys has camel case keys'
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['test-key': 'pass']) == [testKey: 'pass']

        and: 'map with hypenated keys and camel case has camel case keys'
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap([
                'test-key': 'pass', another: 'more pass', andAnother: 'yay'
        ]) == [testKey: 'pass', another: 'more pass', andAnother: 'yay']

        and: 'nested map with hypenated keys has camel case keys all sorted'
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap([
                'test-key': 'pass', 'going-in': [
                'sub-key': 'more', hello: [
                'another': 'bye', 'and-another': 'passing'],
        ]
        ]) == [
                'testKey': 'pass', 'goingIn': [
                'subKey': 'more', hello: [
                'another': 'bye', 'andAnother': 'passing'],
        ]
        ]
    }

    def 'test preprocessing when entries contain lists of maps'() {

        when: 'map is nested maps and lists'
        Map unprocessed = [
                a: ['a-a': 'a', 'a-b': 'b'],
                b: ['b-a': 'c', 'b-b': ['b-b-b': 'd'], 'b-c': [
                        ['c-a': ['c-a-a': 'e', 'c-a-b': 'f']],
                        ['d-a': ['g']]
                ]]
        ]

        then:
        CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(unprocessed) ==
        [
                a: ['aA': 'a', 'aB': 'b'],
                b: ['bA': 'c', 'bB': ['bBB': 'd'], 'bC': [
                        ['cA': ['cAA': 'e', 'cAB': 'f']],
                        ['dA': ['g']]
                ]]
        ]

    }

    def 'test create valid key no mappings'() {

        expect: 'no mapping exists key is unchanged'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', [:]) == 'test'

        and: 'null mapping exists key is unchanged'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', null) == 'test'

        and: 'no mapping with hypenated key fixes hypenation'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('testKey', [:]) == 'testKey'

        and: 'no mapping with camel case key stays the same'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('testKey', null) == 'testKey'

        and: 'no mapping with TitleCase camel fixes key'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('TestKey', null) == 'testKey'
    }

    def 'test create valid key simple mapping'() {

        expect: 'mapping is 1 to 1'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', [test: 'passTest']) == 'passTest'

        and: 'more than 1 mapping'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', [test: 'passTest', fail: 'failTest']) == 'passTest'

        and: 'mapping but not relevant'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', [fail: 'failTest']) == 'test'
    }

    def 'test create valid key mapping from mapping'() {

        expect: 'simple mapping is contained'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', [test: [subkey: 'passTest', another: 'failTest']]) ==
        [subkey: 'passTest', another: 'failTest']

        and: 'complex mapping is contained'
        CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test',
                                                                [test: [complex: [
                                                                        subkey : 'passTest',
                                                                        another: 'failTest']]
                                                                ]
        ) == [complex: [subkey: 'passTest', another: 'failTest']]
    }


    def 'test extract keys'() {

        given: 'correctly extracted simple name mapping key and valid key'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)

        expect: 'keys to be map'
        keys == ['subKey': 'subKey']
    }

    def 'test extract key value pair from single step mapping with single mapping'() {

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key': 'passTest'])

        when: 'extracting key value pair'
        Map keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == 'passTest'
    }

    def 'test extract key value pair from single step mapping with single mapping but no value'() {

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key': null])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == null
    }

    def 'test extract key value pair from single step mapping with single mapping but different value'() {

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['bad-key': 'failTest'])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == null
    }

    def 'test extract key value pair from single step mapping with single mapping and empty value'() {

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = [:]

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == null
    }

    def 'test extract key value pair from single step mapping with single mapping and null value'() {

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(['test.subKey:subKey'] as String[])
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = null

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == null
    }

    def 'test extract key value pair from single step mapping with multiple key mappings'() {

        def nameMappings = ['test.subKey:subKey', 'test.badKey:badKey', 'test.failKey:failKey'] as String[]

        given: 'correctly extracted simple name mapping key and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(nameMappings)
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key': 'passTest'])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey == 'passTest'
    }

    def 'test extract key value pair from multiple step single name mapping entry'() {

        def nameMappings = [
                'test.subKey1.entry1:subKey1Entry1',] as String[]

        given: 'correctly extracted name mapping keys and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(nameMappings)
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key1': ['entry1': 'passTest']])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey1Entry1 == 'passTest'
    }

    def 'test extract key value pair from multiple step multiple name mapping entries'() {

        def nameMappings = [
                'test.subKey1.entry1:subKey1Entry1',
                'test.subKey2.entry2:subKey2Entry2',] as String[]

        given: 'correctly extracted name mapping keys and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(nameMappings)
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key1': ['entry1': 'passTest']])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey1Entry1 == 'passTest'
    }

    def 'test extract key value pair from multiple step multiple name mapping entries with a map value'() {

        def nameMappings = [
                'test.subKey1.entry1:subKey1Entry1',
                'test.subKey2.entry2:subKey2Entry2',] as String[]

        given: 'correctly extracted name mapping keys and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(nameMappings)
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.
                preProcessDataBindingMap(['sub-key1': ['entry1': [subEntry: 'passTest',
                                                                  another : 'passTest']]])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey1Entry1 == [subEntry: 'passTest', another: 'passTest']
    }

    def 'test extract key value pair when multiple name mappings and value is not first in mappings'() {
        def nameMappings = [
                'test.subKey1.entry1:subKey1Entry1',
                'test.subKey2.entry2:subKey2Entry2',] as String[]

        given: 'correctly extracted name mapping keys and valid key and single entry value map'
        Map<String, Object> keyMappings = creator.extractNameMappings(nameMappings)
        def keys = CaseAdjustingXmlDataBindingSourceCreator.createValidKey('test', keyMappings)
        def value = CaseAdjustingXmlDataBindingSourceCreator.preProcessDataBindingMap(['sub-key2': ['entry2': 'passTest']])

        when: 'extracting key value pair'
        Map<String, Object> keyValue = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping(keys, value)

        then: 'the key and value are correct'
        keyValue.subKey2Entry2 == 'passTest'
    }

    def 'test processing of map using class with no mapping'() {

        given:
        Map<String, Object> preprocessed = [pass: 'value']
        Class bindingTargetType = NoProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
    }

    def 'test processing of two map using class with no mapping'() {

        given:
        Map<String, Object> preprocessed = [pass: 'value', wibble: 'wobble']
        Class bindingTargetType = NoProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        processed.wibble == 'wobble'
    }

    def 'test processing of map using class with single mapping'() {

        given:
        Map<String, Object> preprocessed = [fail: 'value']
        Class bindingTargetType = SingleProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
    }

    def 'test processing of map using class with two mappings'() {

        given:
        Map<String, Object> preprocessed = [fail: 'value', another: 'wobble']
        Class bindingTargetType = TwoProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
        processed.wibble == 'wobble'
        !processed.another
    }

    def 'test processing of map using class with mixed simple mappings'() {

        given:
        Map<String, Object> preprocessed = [fail: 'value', another: 'wobble', colour: 'black']
        Class bindingTargetType = MixedSimpleProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
        processed.wibble == 'wobble'
        !processed.another
        processed.colour == 'black'
    }

    def 'test processing of map using class with single nested mapping'() {

        given:
        Map<String, Object> preprocessed = [nest: [fail: 'value']]
        Class bindingTargetType = SingleNestedProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
    }

    def 'test processing of map using class with multiple same nested mapping'() {

        given:
        Map<String, Object> preprocessed = [nest: [fail: 'value', another: 'wobble']]
        Class bindingTargetType = TwoMultipleSameProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
        processed.wibble == 'wobble'
        !processed.another
    }

    def 'test processing of map using class with multiple same nested and no mapping'() {

        given:
        Map<String, Object> preprocessed = [nest: [fail: 'value', another: 'wobble'], colour: 'black']
        Class bindingTargetType = TwoMultipleSameMixedProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
        processed.wibble == 'wobble'
        !processed.another
        processed.colour == 'black'
    }

    def 'test processing of map using class with multiple different nested mapping'() {

        given:
        Map<String, Object> preprocessed = [nest: [fail: 'value'], diff: [another: 'wobble']]
        Class bindingTargetType = TwoMultipleDifferentProcessTest


        when:
        Map<String, Object> processed = creator.processDataBindingMap(preprocessed, bindingTargetType)

        then:
        processed.pass == 'value'
        !processed.fail
        processed.wibble == 'wobble'
        !processed.another
    }

    def 'test extracting key value pair'() {

        when:
        Map res = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping([fail: 'pass'], [fail: 'value'])

        then:
        res.pass == 'value'

        when:
        res = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping([fail: 'pass', diff: 'fail'], [fail: 'value'])

        then:
        res.pass == 'value'

        when:
        res = CaseAdjustingXmlDataBindingSourceCreator.extractKeyValuePairFromKeyMapping([fail: 'pass', diff: 'fail'], [fail: 'value'])

        then:
        res.pass == 'value'

        when:
        res = CaseAdjustingXmlDataBindingSourceCreator.
                extractKeyValuePairFromKeyMapping([fail: 'pass', diff: 'another'], [fail: 'value', diff: 'value2'])

        then:
        res.pass == 'value'
        res.another == 'value2'


    }
}

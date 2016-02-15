package uk.ac.ox.ndm.utils.grails.validator

import spock.lang.Specification

/**
 * @since 07/12/2015
 */
class StringListPatternValidatorTest extends Specification {


    StringListPatternValidator validator

    def setup() {
        validator = new StringListPatternValidator('test', '(valid|string)')
    }

    void 'empty list is valid'() {
        expect:
        assert validator.isValid([]) instanceof Boolean
    }

    void 'list with 1 valid entry is valid'() {
        expect:
        assert validator.isValid(['valid']) instanceof Boolean
    }

    void 'list with 2 valid entries is valid'() {
        expect:
        assert validator.isValid(['valid', 'string']) instanceof Boolean
    }

    void 'list with 1 invalid entry is invalid'() {
        expect:
        assert validator.isValid(['invalid'])[0] == 'validation.list.pattern.invalid'
    }

    void 'list with 2 invalid entries is invalid'() {
        expect:
        assert validator.isValid(['invalid', 'entry'])[0] == 'validation.list.pattern.invalid'
    }

    void 'list with 1 invalid entry and 1 valid is invalid'() {
        expect:
        assert validator.isValid(['invalid', 'string'])[0] == 'validation.list.pattern.invalid'
    }

    void 'list with 1 invalid entry and 2 valid is invalid'() {
        expect:
        assert validator.isValid(['invalid', 'string', 'valid'])[0] == 'validation.list.pattern.invalid'
    }

    void 'list with 2 invalid entry and 2 valid is invalid'() {
        expect:
        assert validator.isValid(['invalid', 'string', 'valid', 'entry'])[0] == 'validation.list.pattern.invalid'
    }
}

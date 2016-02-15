package uk.ac.ox.ndm.utils.grails.validator

import grails.util.Pair

/**
 * @since 24/11/2015
 */
class AtLeastOneValidator implements Validator<Pair<Object, Object[]>> {

    String name

    AtLeastOneValidator(String name) {
        this.name = name
    }

    @Override
    Object isValid(Pair<Object, Object[]> value) {

        Object[] otherValues = value.bValue ?: [] as Object[]

        if (!value.aValue && !otherValues.any()) {
            return ["validation.choice.atleastone", name]
        }
        true
    }

    Object isValid(Object value, Object... choices) {
        isValid new Pair<Object, Object[]>(value, choices)
    }
}

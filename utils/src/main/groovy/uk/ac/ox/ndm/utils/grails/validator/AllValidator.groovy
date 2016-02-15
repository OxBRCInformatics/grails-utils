package uk.ac.ox.ndm.utils.grails.validator

import grails.util.Pair

/**
 * @since 24/11/2015
 */
class AllValidator implements Validator<Pair<Object, Object[]>> {

    String name
    Boolean optional

    AllValidator(String name, Boolean optional = false) {
        this.name = name
        this.optional = optional
    }

    @Override
    Object isValid(Pair<Object, Object[]> value) {

        Object[] otherValues = value.bValue ?: [] as Object[]

        if (optional && !value.aValue && !otherValues.every()) {
            return true
        }
        if (!value.aValue || !otherValues.every()) {
            return ["validation.choice.all", name]
        }
        true
    }

    Object isValid(Object value, Object... choices) {
        isValid new Pair<Object, Object[]>(value, choices)
    }
}

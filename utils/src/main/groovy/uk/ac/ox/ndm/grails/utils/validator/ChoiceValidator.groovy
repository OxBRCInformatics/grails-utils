package uk.ac.ox.ndm.grails.utils.validator

import grails.util.Pair

/**
 * @since 28/08/2015
 */
class ChoiceValidator extends AtLeastOneValidator {

    boolean optional

    ChoiceValidator(String name, boolean optional = false) {
        super(name)
        this.optional = optional
    }

    @Override
    Object isValid(Pair<Object, Object[]> value) {

        Object[] otherValues = value.bValue ?: [] as Object[]

        if (value.aValue && otherValues.any()) {
            return ["validation.choice.onlyone", name]
        }
        if (otherValues.findAll().size() > 1) {
            return ["validation.choice.onlyone", name]
        }

        if (!optional) {
            return super.isValid(value)
        }
        true
    }
}

package uk.ac.ox.ndm.grails.utils.validator

import grails.validation.AbstractVetoingConstraint
import org.springframework.validation.Errors
import org.springframework.validation.FieldError

/**
 * Taken from http://asoftwareguy.com/2013/07/01/grails-cascade-validation-for-pogos/
 *
 *
 */
@Deprecated
class CascadeValidationConstraint extends AbstractVetoingConstraint {

    public static final String NAME = "cascadeValidation"

    @Override
    String getName() {
        NAME
    }

    @Override
    boolean supports(Class type) {
        true
    }

    @Override
    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Boolean)) {
            throw new IllegalArgumentException(
                    """Parameter for constraint [$name] of
                   property [$constraintPropertyName]
                   of class [$constraintOwningClass]
                   must be a Boolean
                """
            )
        }
        super.setParameter(constraintParameter)
    }

    @Override
    protected boolean skipNullValues() {
        return true
    }

    @Override
    protected boolean processValidateWithVetoing(
            Object target, Object propertyValue,
            Errors errors) {
        if (!propertyValue.validate()) {
            propertyValue.errors.fieldErrors.each {
                String field = "${propertyName}.${it.field}"
                def fieldError = new FieldError(
                        target.errors.objectName,
                        field,
                        it.rejectedValue,
                        it.bindingFailure,
                        it.codes,
                        it.arguments,
                        it.defaultMessage
                )
                errors.addError(fieldError)
            }
            return false
        }
        return true
    }
}

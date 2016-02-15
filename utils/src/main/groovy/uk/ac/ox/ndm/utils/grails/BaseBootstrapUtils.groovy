package uk.ac.ox.ndm.utils.grails

import grails.validation.ConstrainedProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import uk.ac.ox.ndm.utils.domain.DataType
import uk.ac.ox.ndm.utils.grails.validator.CascadeValidationConstraint

/**
 * @since 10/09/2015
 */
trait BaseBootstrapUtils {

    Logger getLogger() {
        LoggerFactory.getLogger(getClass())
    }

    abstract Map<String, Class<DataType>[]> getKnownDataTypes()

    abstract void bootstrapTestParticipantIdentifiers(MessageSource messageSource)

    abstract void bootstrapTestData(MessageSource messageSource)

    def registerConstraints() {
        ConstrainedProperty.registerNewConstraint(
                CascadeValidationConstraint.NAME,
                CascadeValidationConstraint.class
        )
    }

    def outputDomainErrors(def domainObj, MessageSource messageSource) {
        logger.error 'Errors validating domain: {}', domainObj.class.simpleName
        System.err.println 'Errors validating domain: ' + domainObj.class.simpleName
        domainObj.errors.allErrors.each {error ->
            if (messageSource) {
                logger.error messageSource.getMessage(error, Locale.default)
                System.err.println messageSource.getMessage(error, Locale.default)
            }
            else {
                logger.error error.defaultMessage
                logger.error "${Arrays.asList(error.arguments)}"
            }
        }
    }

    boolean check(def domainObj, MessageSource messageSource) {

        boolean valid = domainObj.validate()

        if (!valid) {
            outputDomainErrors(domainObj, messageSource)
            return false
        }
        true
    }

    boolean checkAndSave(def domainObj, MessageSource messageSource) {
        if (!check(domainObj, messageSource)) return false
        domainObj.save(failOnError: true) ? true : false
    }

    void bootstrapDataTypes(Collection<String> types) {
        bootstrapDataTypes(getKnownDataTypes().findAll {it.key in types})
    }

    void bootstrapDataTypes(Map datatypes) {
        def values = datatypes.values().flatten()
        values.each {dtCls ->
            dtCls.saveAll dtCls.getDataTypeEnum().values().collect {it.dataTypeObject}
        }
    }

    void bootstrapDataTypesStartingWith(String prefix) {
        bootstrapDataTypes(getKnownDataTypes().findAll {it.key.startsWith(prefix)})
    }

    void bootstrapDataTypes(String... types) {
        bootstrapDataTypes(Arrays.asList(types))
    }

    static String generateRandomNHSNumber() {
        Random random = new Random()
        boolean found = false
        String number = ''
        while (!found) {
            number = random.nextInt().abs() as String
            found = number.length() == 10 && isValidNHSNumber(number)
        }

        number
    }

    static boolean isValidNHSNumber(String nhsNumberStr) {
        if (nhsNumberStr) {
            int checkDigit = 0
            def nhsNumber = nhsNumberStr*.toInteger()
            if (nhsNumber.size() == 10) {
                (0..8).each {
                    checkDigit += nhsNumber[it] * (10 - it)
                }
                checkDigit = (11 - (checkDigit % 11))
                if (checkDigit == 11) checkDigit = 0
                if (checkDigit != 10) {
                    return checkDigit == nhsNumber[9]
                }
            }
        }
        return false
    }
}

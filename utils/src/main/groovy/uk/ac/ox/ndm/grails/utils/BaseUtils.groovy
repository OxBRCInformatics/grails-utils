package uk.ac.ox.ndm.grails.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource

/**
 * @since 20/09/2017
 */
trait BaseUtils {

    Logger getLogger() {
        LoggerFactory.getLogger(getClass())
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
        if (!domainObj) return true

        boolean valid = domainObj.validate()

        if (!valid) {
            outputDomainErrors(domainObj, messageSource)
            return false
        }
        true
    }

    boolean checkAndSave(def domainObj, MessageSource messageSource) {
        if (!domainObj) return false
        if (!check(domainObj, messageSource)) return false
        domainObj.save(failOnError: true) ? true : false
    }
}

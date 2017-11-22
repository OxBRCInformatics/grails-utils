package uk.ac.ox.ndm.grails.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.validation.FieldError

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

            String msg = messageSource ? messageSource.getMessage(error, Locale.default) :
                         "${error.defaultMessage} :: ${Arrays.asList(error.arguments)}"

            if (error instanceof FieldError) msg += " [${error.field}]"

            logger.error msg
            System.err.println msg
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
        save(domainObj)
    }

    boolean save(def domainObj) {
        domainObj.save(failOnError: true, validate: false, flush: true) ? true : false
    }
}

package uk.ac.ox.ndm.grails.utils.test

import org.spockframework.util.Assert
import spock.lang.Specification
import uk.ac.ox.ndm.grails.utils.BaseUtils

/**
 * @since 20/09/2017
 */
abstract class DomainSpec extends Specification implements BaseUtils {

    def setup() {
        logger.warn("--- ${specificationContext.currentIteration.name} ---")
    }

    def outputDomainErrors(def domainObj) {
        outputDomainErrors(domainObj, messageSource)
    }

    boolean checkAndSave(def domainObj) {
        return checkAndSave(domainObj, messageSource)
    }

    boolean validateTestDomain(def domainObj) {
        logger.info "Validating test domain: ${domainObj.class}"
        check(domainObj, messageSource)
    }

    boolean check(def domainObj) {
        if (!check(domainObj, messageSource)) {
            Assert.fail("Could not validate domain")
            false
        }
        true
    }

    def addMessageCode(String messageCode, String message) {
        messageSource.addMessage(messageCode, Locale.default, message)
    }
}

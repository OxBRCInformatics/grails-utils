package uk.ac.ox.ndm.grails.utils.test

import org.spockframework.util.Assert
import org.springframework.context.MessageSource
import spock.lang.Specification
import uk.ac.ox.ndm.grails.utils.BaseBootstrapUtils
import uk.ac.ox.ndm.grails.utils.domain.DataType

/**
 * @since 15/09/2015
 */
abstract class CoreSpec extends Specification implements BaseBootstrapUtils {

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

    @Override
    Map<String, Class<DataType>[]> getKnownDataTypes() {
        [core: [] as Class[]]
    }

    @Override
    void bootstrapTestParticipantIdentifiers(MessageSource messageSource) {

    }

    @Override
    void bootstrapTestData(MessageSource messageSource) {

    }
}

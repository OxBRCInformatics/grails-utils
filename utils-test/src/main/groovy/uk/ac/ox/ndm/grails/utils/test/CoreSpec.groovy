package uk.ac.ox.ndm.grails.utils.test

import org.spockframework.util.Assert
import org.springframework.context.MessageSource
import spock.lang.Shared
import spock.lang.Specification
import uk.ac.ox.ndm.grails.utils.BaseBootstrapUtils
import uk.ac.ox.ndm.grails.utils.domain.DataType

import java.time.LocalDate

/**
 * @since 15/09/2015
 */
abstract class CoreSpec extends Specification implements BaseBootstrapUtils {

    @Shared
    Map pid1Map = [
            nhsNumber    : '1297358368',
            forenames    : 'Test',
            surname      : 'User 1',
            dateOfBirth  : LocalDate.of(1980, 02, 03),
            participantId: 'id1'
    ]

    @Shared
    Map pid2Map = [
            nhsNumber    : '1440618771',
            forenames    : 'Test',
            surname      : 'User 2',
            dateOfBirth  : LocalDate.of(1984, 11, 23),
            participantId: 'id2'
    ]

    @Shared
    Map testEventDetailMap = [
            organisationId  : 'test_org',
            eventReference  : 'test_ref',
            primaryDiagnosis: 'test_diag',
            eventDate       : LocalDate.of(2015, 8, 20)
    ]

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

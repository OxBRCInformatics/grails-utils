package uk.ac.ox.ndm.grails.utils.test

import org.springframework.context.MessageSource
import uk.ac.ox.ndm.grails.utils.BaseBootstrapUtils
import uk.ac.ox.ndm.grails.utils.domain.DataType

/**
 * @since 15/09/2015
 */
@Deprecated
abstract class CoreSpec extends DomainSpec implements BaseBootstrapUtils {

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

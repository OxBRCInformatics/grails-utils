package uk.ac.ox.ndm.grails.utils.rabbitmq

import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.fs.FileSystemAssetResolver
import org.xml.sax.SAXException
import spock.lang.Ignore
import spock.lang.Specification

import javax.xml.validation.Schema
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * @since 27/04/2016
 */
class AbstractRabbitMqXmlValidationServiceTest extends Specification {

    AbstractRabbitMqXmlValidationService service

    def setup() {
        def resolver1 = new FileSystemAssetResolver('testResolver1', "src/test/resources")
        def resolver2 = new FileSystemAssetResolver('testResolver2', "utils/src/test/resources")
        AssetPipelineConfigHolder.registerResolver(resolver1)
        AssetPipelineConfigHolder.registerResolver(resolver2)
        service = new TestService()
        service.initialise()
    }

    void 'test get schemas'() {
        when: 'getting schemas'
        def schemas = service.getSchemas()

        then:
        schemas.size() == 11
    }

    void 'test empty validating xml'() {

        expect: 'null object fails'
        service.validatesXml(null, null) == null

        and: 'empty string fails'
        service.validatesXml(null, '') == null

    }

    void 'loading valid xml against a valid schema'() {

        when:
        String xml = readFileAsString('test_valid.xml')

        then:
        service.validatesXml(null, xml) == 'RegistrationAndConsentsCancer'

        when:
        xml = readFileAsUTF8String('test_valid.xml')

        then:
        service.validatesXml(null, xml) == 'RegistrationAndConsentsCancer'

    }

    void 'loading invalid xml against a valid schema'() {

        when:
        String xml = readFileAsString('test_invalid.xml')

        then:
        service.validatesXml(null, xml) == null

        when:
        xml = readFileAsUTF8String('test_invalid.xml')

        then:
        service.validatesXml(null, xml) == null

    }

    @Ignore('Currently ut16 issue in place')
    void 'loading utf16 valid xml against a valid schema'() {

        when:
        String xml = readFileAsUTF16String('test_utf16_valid.xml')

        then:
        service.validatesXml(null, xml) == 'RegistrationAndConsentsCancer'

        when:
        xml = readFileAsString('test_utf16_valid.xml')

        then:
        service.validatesXml(null, xml) == 'RegistrationAndConsentsCancer'

    }

    byte[] readFileAsBytes(String filename) {
        byte[] bytes = Files.readAllBytes(Paths.get('src/test/resources/xml', filename).toAbsolutePath())
        if (!bytes) bytes = Files.readAllBytes(Paths.get('utils/src/test/resources/xml', filename).toAbsolutePath())
        bytes
    }

    String readFileAsString(String filename) {
        new String(readFileAsBytes(filename), Charset.defaultCharset())
    }

    String readFileAsUTF8String(String filename) {
        new String(readFileAsBytes(filename), 'utf-8')
    }

    String readFileAsUTF16String(String filename) {
        new String(readFileAsBytes(filename), 'utf-16')
    }

}

class TestService extends AbstractRabbitMqXmlValidationService {

    TestService() {
        super('SchemaCancer-v2.0.0.xsd', ['DataTypesCancer-v2.0.0.xsd', 'SchemaCancer-v2.0.0.xsd'])
    }

    String getDefaultApplicationName() {
        'gel-cancer-v2_0'
    }

    @Override
    boolean handleValidationFailure(String referenceId, String name, Schema schema, SAXException exception) {
        false
    }

    String getDefaultRoutingKey() {
        '#.gel.cancer.v2_0'
    }

    String getDefaultExchange() {
        'gel-cancer-v2-0'
    }

    Pattern getSchemaPattern() {
        ~/\w+Cancer-v2\.0\.\d\.xsd/
    }

    @Override
    Pattern getSchemaSuffix() {
        ~/-v2\.0\.\d\.xsd/
    }

    @Override
    Map<String, Map> getExchangeConfiguration() {
        Map<String, Map> config = super.getExchangeConfiguration()
        config.'exchange_gel' = [
                durable   : true,
                autoDelete: true,
                type      : 'topic'
        ]
        config.'exchange_gel-cancer' = [
                durable                : true,
                autoDelete             : true,
                type                   : 'topic',
                'bind-to_gel'          : '#.gel.cancer.#',
                ('bind-to_' + exchange): [
                        binding: routingKey + '.#',
                        as     : 'source'
                ]
        ] as Map<String, Object>
        config
    }
}

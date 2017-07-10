package uk.ac.ox.ndm.grails.utils.rabbitmq

import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.fs.FileSystemAssetResolver
import org.xml.sax.SAXException
import spock.lang.Ignore
import spock.lang.Specification

import javax.xml.validation.Schema
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
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
        service.validateAndGetSchemaName(null, null as String) == null

        and: 'empty string fails'
        service.validateAndGetSchemaName(null, '') == null

    }

    void 'loading valid xml against a valid schema'() {

        when:
        String xml = readFileAsString('test_valid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == 'RegistrationAndConsentsCancer-v2.0.0'

        when:
        xml = readFileAsUTF8String('test_valid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == 'RegistrationAndConsentsCancer-v2.0.0'

    }

    void 'loading invalid xml against a valid schema'() {

        when:
        String xml = readFileAsString('test_invalid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == null

        when:
        xml = readFileAsUTF8String('test_invalid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == null

    }

    @Ignore('Currently ut16 issue in place')
    void 'loading utf16 valid xml against a valid schema'() {

        when:
        String xml = readFileAsUTF16String('test_utf16_valid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == 'RegistrationAndConsentsCancer'

        when:
        xml = readFileAsString('test_utf16_valid.xml')

        then:
        service.validateAndGetSchemaName(null, xml) == 'RegistrationAndConsentsCancer'

    }

    void 'check basic rabbit exchange configuration'() {

        given:
        service = new BasicTestService()
        service.initialise()

        when: 'getting a non-overridden exchange configuration'
        Collection<Exchange> exchanges = service.getExchanges()

        then: 'should be 1 exchange'
        exchanges.size() == 1
        def exchange = exchanges[0]

        and:
        exchange.name == 'gel-cancer-v2-0'
        exchange.type == 'topic'
        exchange.durable
        exchange.autoDelete
    }

    void 'check basic rabbit queues configuration'() {

        given:
        service = new BasicTestService()
        service.initialise()

        when: 'getting a non-overridden queues configuration'
        Collection<Queue> queues = service.getQueues()

        then: 'should be queues for each schema'
        queues.size() == 1
        def queue = queues[0]

        and:
        queue.name == 'gel-cancer-v2-0-careplanscancer'
        queue.exchange == 'gel-cancer-v2-0'
        queue.durable
        queue.autoDelete
        queue.binding == '#.gel.cancer.v2_0.careplanscancer'
    }

    void 'check more complicated rabbit exchange configuration'() {

        when: 'getting a non-overridden exchange configuration'
        Collection<Exchange> exchanges = service.getExchanges()

        then: 'should be 3 exchanges'
        exchanges.size() == 3
        def exchange = exchanges[0]
        def exchangeGel = exchanges[1]
        def exchangeGelCancer = exchanges[2]

        and:
        exchange.name == 'gel-cancer-v2-0'
        exchange.type == 'topic'
        exchange.durable
        exchange.autoDelete

        and:
        exchangeGel.name == 'gel'
        exchangeGel.type == 'topic'
        exchangeGel.durable
        exchangeGel.autoDelete

        and:
        exchangeGelCancer.name == 'gel-cancer'
        exchangeGelCancer.type == 'topic'
        exchangeGelCancer.durable
        exchangeGelCancer.autoDelete
        exchangeGelCancer.exchangeBindings.size() == 1
        ExchangeBinding bindings = exchangeGelCancer.exchangeBindings[0]

        and:
        bindings.origin == Origin.SOURCE
        bindings.exchange == 'gel-cancer-v2-0'
        bindings.binding == '#.gel.cancer.v2_0.#'
    }

    void 'check more complicated rabbit queues configuration'() {

        when: 'getting a non-overridden queues configuration'
        Collection<Queue> queues = service.getQueues()

        then: 'should be queues for each schema'
        queues.size() == 11
        def queue = queues[0]

        and:
        queue.name == 'gel-cancer-v2-0-careplanscancer'
        queue.exchange == 'gel-cancer-v2-0'
        queue.durable
        queue.autoDelete
        queue.binding == '#.gel.cancer.v2_0.careplanscancer'
    }

    byte[] readFileAsBytes(String filename) {
        Path p = Paths.get('src/test/resources/xml', filename).toAbsolutePath()
        if (!Files.exists(p)) p = Paths.get('utils/src/test/resources/xml', filename).toAbsolutePath()
        Files.readAllBytes(p)
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

    String getDefaultExchangeName() {
        'gel-cancer-v2-0'
    }

    Pattern getSchemaPattern() {
        ~/\w+Cancer-v2\.0\.\d\.xsd/
    }

    @Override
    Pattern getSchemaSuffix() {
        ~/-v2\.0\.\d/
    }

    @Override
    Collection<Exchange> getExchanges() {
        Collection<Exchange> exchanges = super.getExchanges()
        exchanges + [
                new Exchange(
                        name      : 'gel',
                        durable   : true,
                        autoDelete: true,
                        type      : 'topic'
                ),
                new Exchange(
                        name            : 'gel-cancer',
                        durable         : true,
                        autoDelete      : true,
                        type            : 'topic',
                        exchangeBindings: [
                                new ExchangeBinding(
                                        origin      : Origin.SOURCE,
                                        exchange: getExchangeName(),
                                        binding : getRoutingKey() + '.#'
                                )
                        ]
                )
        ]
    }
}

class BasicTestService extends AbstractRabbitMqXmlValidationService {

    BasicTestService() {
        super('CarePlansCancer-v2.0.0.xsd', ['DataTypesCancer-v2.0.0.xsd'])
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

    String getDefaultExchangeName() {
        'gel-cancer-v2-0'
    }

    Pattern getSchemaPattern() {
        ~/CarePlansCancer-v2\.0\.0\.xsd/
    }

    @Override
    Pattern getSchemaSuffix() {
        ~/-v2\.0\.\d/
    }
}

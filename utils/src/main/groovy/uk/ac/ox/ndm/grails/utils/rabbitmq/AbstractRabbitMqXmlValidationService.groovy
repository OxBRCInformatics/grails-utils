package uk.ac.ox.ndm.grails.utils.rabbitmq

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException
import uk.ac.ox.ndm.grails.utils.xml.XmlValidator
import uk.ac.ox.ndm.grails.utils.xml.resource.ResourceResolver

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import java.util.regex.Pattern

/**
 * @since 01/03/2016
 */
@CompileStatic
abstract class AbstractRabbitMqXmlValidationService implements XmlValidator, RabbitMqRoutingInfomationProvider {

    Logger getLogger() {
        LoggerFactory.getLogger(getClass())
    }

    abstract Pattern getSchemaPattern()

    abstract Pattern getSchemaSuffix()

    abstract String getDefaultExchange()

    abstract String getDefaultRoutingKey()

    abstract String getDefaultApplicationName()

    abstract boolean handleValidationFailure(String referenceId, String schemaName, Schema schema, SAXException exception)

    String applicationName

    String routingKey
    String exchange

    Integer priority

    Map<String, Schema> schemas

    protected List<String> mainXsdFilenames
    protected List<String> ignoredSchemas

    private boolean initialised

    public static final Integer DEFAULT_PRIORITY = Integer.MAX_VALUE

    AbstractRabbitMqXmlValidationService(String mainXsdFilename, List<String> ignoredSchemas = [], int priority = DEFAULT_PRIORITY,
                                         boolean deferred = true) {
        this([mainXsdFilename], ignoredSchemas, priority, deferred)
    }

    AbstractRabbitMqXmlValidationService(List<String> mainXsdFilenames, List<String> ignoredSchemas = [], int priority = DEFAULT_PRIORITY,
                                         boolean deferred = true) {
        this.mainXsdFilenames = mainXsdFilenames
        this.ignoredSchemas = ignoredSchemas
        this.schemas = [:]
        this.priority = priority
        initialised = false
        if (!deferred) initialise()
    }

    void initialise() {
        if (initialised) return

        mainXsdFilenames.each {mainXsdFilename ->
            ResourceResolver resourceResolver = new ResourceResolver(mainXsdFilename)
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            factory.setResourceResolver(resourceResolver)

            schemas = resourceResolver.schemaCache.findAll {
                !(it.key in ignoredSchemas) && it.key =~ getSchemaPattern()
            }.collectEntries {filename, contents ->
                def schema = factory.newSchema(new StreamSource(new StringReader(contents), filename))
                [convertFilenameToSchemaKeyName(filename), schema]
            } as Map<String, Schema>
        }
        initialised = true
    }

    /**
     * Convert the name (which is a filename) to the schema key name.
     * This will also be used as the queue name suffix.
     *
     * If overriding then call this super method then alter the returned string.
     */
    String convertFilenameToSchemaKeyName(String filename) {
        filename.replaceFirst(getSchemaSuffix(), '')
    }

    String getExchange() {
        exchange ?: defaultExchange
    }

    String getApplicationName() {
        applicationName ?: defaultApplicationName
    }

    String getRoutingKey() {
        routingKey ?: defaultRoutingKey
    }

    @Override
    String validatesXml(String referenceId, String xml) {
        if (!xml) return null

        schemas.find {name, schema ->
            try {
                schema.newValidator().validate(new StreamSource(new ByteArrayInputStream(xml.bytes)))
            } catch (SAXException ex) {
                if (ex.message.contains('cvc-elt.1')) {
                    logger.debug("{} does not describe submitted XML", name)
                    return false
                }
                if (ex instanceof SAXParseException) {
                    logger.warn('{} does not validate because of {}', name, saxParseExceptionToString(ex as SAXParseException))
                }
                else {
                    logger.debug('{} does not validate because {}', name, ex.toString())
                }
                return handleValidationFailure(referenceId, name, schema, ex)
            }
            true
        }?.key
    }

    @Override
    String validatesXml(String referenceId, GPathResult xml) {
        if (!xml) return null
        validatesXml(referenceId, XmlUtil.serialize(xml))
    }

    @Override
    Map<String, Map> getExchangeConfiguration() {
        [
                ('exchange_' + getExchange()):
                        [
                                type      : 'topic',
                                durable   : true,
                                autoDelete: true,
                                queues    : schemas.collectEntries {name, schema ->
                                    [('queue_' + getExchange().toLowerCase() + '-' + name.toLowerCase()),
                                     [
                                             durable   : true,
                                             autoDelete: true,
                                             binding   : getRoutingKey() + '.' + name.toLowerCase(),
                                     ]
                                    ]
                                }
                        ]
        ] as Map<String, Map>
    }

    @TypeChecked(value = TypeCheckingMode.SKIP)
    Map updateRabbitConfig(Map rabbitConfig) {

        if (!rabbitConfig || !rabbitConfig instanceof Map) throw new IllegalStateException('There must be a defined RabbitMq Map configuration')
        if (!(rabbitConfig.connection || rabbitConfig.connections))
            throw new IllegalStateException('There must be a defined RabbitMq connection or connections configuration')

        Map<String, Map> queuesConfig = rabbitConfig.queues ?: [:]
        Map<String, Map> addExcConfig = exchangeConfiguration

        addExcConfig.each {exchange, config ->
            if (queuesConfig[exchange]) {
                Map existingExchange = queuesConfig[exchange] as Map
                if (existingExchange.queues) {
                    config.queues.each {k, v ->
                        existingExchange.queues[k] = v
                    }
                }
                else existingExchange.queues = config.queues

                config.findAll {((String) it.key).startsWith('bind-to_')}.each {
                    existingExchange[it.key] = it.value
                }

                queuesConfig[exchange] = existingExchange
            }
            else queuesConfig[exchange] = config

        }
        rabbitConfig.queues = queuesConfig
        rabbitConfig
    }

    String saxParseExceptionToString(SAXParseException ex) {
        StringBuilder buf = new StringBuilder();
        String message = ex.getLocalizedMessage();
        if (ex.lineNumber != -1) buf.append("lineNumber: ").append(ex.lineNumber);
        if (ex.columnNumber != -1) buf.append(" & columnNumber: ").append(ex.columnNumber);

        //append the exception message at the end
        if (message != null) buf.append(" - ").append(message);
        return buf.toString();
    }
}

package uk.ac.ox.ndm.grails.utils.rabbitmq

import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xml.sax.SAXException
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
abstract class AbstractRabbitMqXmlValidationService implements XmlValidator, RabbitMqRoutingInfomationProvider {

    Logger getLogger() {
        LoggerFactory.getLogger(getClass())
    }

    abstract Pattern getSchemaPattern()

    abstract String getDefaultExchange()

    abstract String getDefaultRoutingKey()

    abstract String getDefaultApplicationName()

    String applicationName

    String routingKey
    String exchange

    ResourceResolver resourceResolver
    Map<String, Schema> schemas

    protected String mainXsdFilename
    protected List<String> ignoredSchemas

    private boolean initialised

    AbstractRabbitMqXmlValidationService(String mainXsdFilename, List<String> ignoredSchemas = [], boolean deferred = true) {
        this.mainXsdFilename = mainXsdFilename
        this.ignoredSchemas = ignoredSchemas
        this.schemas = [:]
        initialised = false
        if (!deferred) initialise()
    }

    void initialise() {
        if (initialised) return
        resourceResolver = new ResourceResolver(mainXsdFilename)
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        factory.setResourceResolver(resourceResolver)

        schemas = resourceResolver.schemaCache.findAll {
            !(it.key in ignoredSchemas) && it.key =~ getSchemaPattern()
        }.collectEntries {filename, contents ->
            def schema = factory.newSchema(new StreamSource(new StringReader(contents), filename))
            [filename.replaceFirst(getSchemaPattern(), ''), schema]
        }
        initialised = true
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
    String validatesXml(String xml) {
        if (!xml) return null

        schemas.find {name, schema ->
            try {
                schema.newValidator().validate(new StreamSource(new StringReader(xml)))
            } catch (SAXException ignored) {
                logger.debug('{} does not validate because {}', name, ignored.message)
                return false
            }
            true
        }?.key
    }

    @Override
    String validatesXml(GPathResult xml) {
        if (!xml) return null
        validatesXml(XmlUtil.serialize(xml))
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
        ]
    }

    Map updateRabbitConfig(Object rabbitConfig) {

        if (!rabbitConfig || !rabbitConfig instanceof Map) throw new IllegalStateException('There must be a defined RabbitMq Map configuration')
        if (!(rabbitConfig.connection || rabbitConfig.connections))
            throw new IllegalStateException('There must be a defined RabbitMq connection or connections configuration')

        Map queuesConfig = rabbitConfig.queues ?: [:]
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

                config.findAll {((String) it).startsWith('bind-to_')}.each {
                    existingExchange[it.key] = it.value
                }

                queuesConfig[exchange] = existingExchange
            }
            else queuesConfig[exchange] = config

        }
        rabbitConfig.queues = queuesConfig
        rabbitConfig
    }
}

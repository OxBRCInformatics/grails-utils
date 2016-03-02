package uk.ac.ox.ndm.grails.utils.rabbitmq

import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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

    @Value('${info.app.name}')
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
        initialised = false
        if (!deferred) initialise()
    }

    void initialise() {
        if (initialised) return
        resourceResolver = new ResourceResolver(mainXsdFilename)
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        factory.setResourceResolver(resourceResolver)

        schemas = resourceResolver.schemaCache.findAll {!(it.key in ignoredSchemas)}.collectEntries {filename, contents ->
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
        routingKey && routingKey != '@info.app.name@' ? routingKey : defaultRoutingKey
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
}
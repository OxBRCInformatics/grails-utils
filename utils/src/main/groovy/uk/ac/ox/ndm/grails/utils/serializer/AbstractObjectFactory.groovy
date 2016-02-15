package uk.ac.ox.ndm.grails.utils.serializer

import com.google.common.base.CaseFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.ac.ox.ndm.grails.utils.Utils
import uk.ac.ox.ndm.grails.utils.domain.DataType

import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar
import java.time.LocalDate

/**
 * @since 09/09/2015
 */
abstract class AbstractObjectFactory implements Utils {

    Object buildModel(JaxbSerializable originalModel, String key, Map<String, String> factoryMethodMappings)
            throws IllegalArgumentException {

        String method = "create${CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, key)}"

        if (key in factoryMethodMappings.keySet()) {
            method = "create${factoryMethodMappings."$key"}"
        }

        try {
            return buildModel(originalModel, this."$method"())
        } catch (MissingMethodException ex) {
            logger.error("Could not find method '{}' in ObjectFactory", method)
            logger.error "Exception: {}", ex.message
            throw ex
        }
    }

    Object buildModel(JaxbSerializable originalModel, JaxbSerializeObject buildingModel) throws IllegalArgumentException {
        if (buildingModel == null) {
            throw new IllegalArgumentException('Model being built cannot be null')
        }
        logger.debug("Building model[{}] from model[{}]", buildingModel.class.canonicalName, originalModel.class.canonicalName)

        Map<String, Object> buildProps = buildingModel.properties.findAll {
            //noinspection GroovyInArgumentCheck
            !(it.key in ['class', 'logger'])
        }.sort()

        String[] factoryMappings = originalModel.class.getAnnotation(SerializeMappings)?.factoryMethodMappings()
        Map<String, String> factoryMethodMappings = factoryMappings ? factoryMappings.collectEntries {it ->
            def split = it.split(':')
            [split[0], split[1]]
        } : [:]

        Map<String, Object> mappings = extractNameMappings(originalModel.class.getAnnotation(SerializeMappings)?.nameMappings())

        Map<String, Object> props = buildProps.collectEntries {k, v ->
            try {
                return [k, originalModel."$k"]
            } catch (MissingPropertyException ignored) {}

            if (mappings."$k" && mappings."$k" instanceof String) {
                String key = mappings."$k"

                if (originalModel.hasProperty(key)) {
                    return [k, originalModel."$key"]
                }
            }
            [:]
        }

        props.each {String key, value ->
            addToBuildModel(buildingModel, key, value, factoryMethodMappings)
        }

        buildingModel
    }

    protected void addToBuildModel(def buildingModel, String key, Object value, Map<String, String> factoryMethodMappings)
            throws IllegalArgumentException {

        logger.trace("Build model: {} >> Key: {}, value: {} [{}]", buildingModel.class.simpleName, key,
                     (value instanceof Map ? "Map:size=${value.size()}" :
                      value instanceof Collection ? "Collection:size=${value.size()}" : value),
                     value?.class)

        if (value) {
            if (value instanceof JaxbSerializable) {
                buildingModel."$key" = buildModel(value, key, factoryMethodMappings)
            }
            else if (value instanceof DataType) {
                try {
                    buildingModel."$key" = value.findEnumValue()
                } catch (IllegalArgumentException ignored) {
                    logger.warn "Cannot render XSD limited value '{}' for datatype '{}'", value.id, value.class.canonicalName
                }
            }
            else if (value instanceof Map) {
                value.each {sKey, sValue ->
                    def putValue
                    try {
                        putValue = buildModel(sValue, key, factoryMethodMappings)
                    } catch (IllegalArgumentException ignored) {
                        //putValue = sValue
                        throw ignored
                    }
                    buildingModel."$key".put sKey, putValue
                }
            }
            else if (value instanceof Collection) {
                value.each {entry ->
                    def addValue
                    try {
                        addValue = buildModel(entry, key, factoryMethodMappings)
                    } catch (IllegalArgumentException ex) {
                        //addValue = entry
                        throw ex
                    }
                    buildingModel."$key".add addValue
                }
            }
            else {
                try {
                    buildingModel."$key" = value
                } catch (ClassCastException ex) {
                    if (value instanceof Date || value instanceof LocalDate) {
                        buildingModel."$key" = convertToXmlGregorianCalendar(value)
                    }
                    else {
                        logger.warn("Could not save '{}' to build model: {}", key, ex.getMessage())
                    }
                }
            }

        }
    }

    protected Logger getLogger() {
        LoggerFactory.getLogger(getClass());
    }

    static XMLGregorianCalendar convertToXmlGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar()
        cal.setTime(date)
        cal.setTimeZone(TimeZone.getTimeZone("UTC"))
        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
    }

    static XMLGregorianCalendar convertToXmlGregorianCalendar(LocalDate date) {
        GregorianCalendar cal = new GregorianCalendar()
        DatatypeFactory.newInstance().newXMLGregorianCalendar(date.toString())
    }
}

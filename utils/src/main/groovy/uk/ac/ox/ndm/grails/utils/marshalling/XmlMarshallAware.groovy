package uk.ac.ox.ndm.grails.utils.marshalling

import com.google.common.base.CaseFormat
import grails.converters.XML
import grails.converters.XML.Builder
import org.grails.web.converters.ConverterUtil
import org.grails.web.converters.configuration.ConverterConfiguration
import org.grails.web.converters.configuration.ConvertersConfigurationHolder
import uk.ac.ox.ndm.grails.utils.domain.DataType

/**
 * @since 27/10/2015
 */
trait XmlMarshallAware {

    protected XML xml
    private final ConverterConfiguration<XML> config = ConvertersConfigurationHolder.getConverterConfiguration(XML.class)

    String xmlElementName() {
        CaseFormat.UPPER_CAMEL.to CaseFormat.LOWER_CAMEL, getClass().simpleName
    }

    void marshallObject(XML xml) {
        this.xml = xml
        marshall()
    }

    abstract void marshall()

    void chars(String string) {
        xml.chars string
    }

    void child(XmlMarshallAware childObject) {
        child childObject.xmlElementName(), childObject
    }

    void child(String name, Object childObject) {
        xml.startNode name
        xml.convertAnother childObject
        xml.end()
    }

    void child(String name, @DelegatesTo(Builder) Closure closure) {
        xml.startNode name
        xml.build(closure)
        xml.end()
    }

    void optionalChild(XmlMarshallAware childObject) {
        if (childObject) child childObject
    }

    void optionalChild(String name, Object childObject) {
        if (childObject) child name, childObject
    }

    void optionalChild(String name, @DelegatesTo(Builder) Closure closure) {
        xml.startNode name
        xml.build(closure)
        xml.end()
    }

    void childList(String childName, List list) {
        list?.each {child childName, it}
    }

    void childList(List list) {
        list?.each {child it}
    }

    void childCollection(String childName, Collection collection) {
        if (collection) {
            collection[0].hasProperty('id') ?
            childList(childName, collection?.sort {it?.id}) :
            childList(childName, collection?.sort())
        }
    }

    void childCollection(Collection collection) {
        if (collection) {
            collection[0].hasProperty('id') ?
            childList(collection?.sort {it?.id}) :
            childList(collection?.sort())
        }
    }

    @Deprecated
    void childList(String childName, Set collection) {
        childCollection(childName, collection)
    }

    @Deprecated
    void childList(Set collection) {
        childCollection(collection)
    }

    void choice(Map<String, Object> optionObjects) {
        def childObject = optionObjects.find {k, v -> v}
        child childObject.key, childObject.value
    }

    void choiceChild(String childName, Map<String, Object> optionObjects) {
        xml.startNode childName
        choice(optionObjects)
        xml.end()
    }

    void optionalChoice(Map<String, Object> optionObjects) {
        def childObject = optionObjects.find {k, v -> v}
        if (childObject) child childObject.key, childObject.value
    }

    void childAttribute(String name, String attributeName, Object childObject) {
        if (childObject) {
            xml.startNode name
            attribute attributeName, childObject
            xml.end()
        }
    }

    void attribute(String name, Object childObject) {
        xml.attribute name, convertToString(childObject)
    }

    void startNode(String tagName) {
        xml.startNode tagName
    }

    void end() {
        xml.end()
    }

    void endNode() {
        xml.end()
    }

    void build(@DelegatesTo(Builder) Closure closure) {
        xml.build closure
    }

    void expandChild(String name, XmlMarshallAware... childObject) {
        xml.startNode name
        childObject*.marshallObject(xml)
        xml.end()
    }

    String convertToString(Object childObject) {
        childObject = config.getProxyHandler().unwrapIfProxy(childObject);

        try {
            if (childObject == null) return null
            if (childObject instanceof CharSequence) return childObject.toString()
            if (childObject instanceof Class<?>) return ((Class<?>) childObject).getName()
            if ((childObject.getClass().isPrimitive() && !childObject.getClass().equals(byte[].class)) ||
                childObject instanceof Number || childObject instanceof Boolean) return String.valueOf(childObject)
            if (childObject instanceof DataType) return childObject.id
            return childObject.toString()
        }
        catch (Throwable t) {
            throw ConverterUtil.resolveConverterException(t);
        }
    }
}

package uk.ac.ox.ndm.grails.utils.marshalling

import grails.converters.XML
import org.grails.web.converters.exceptions.ConverterException
import org.grails.web.converters.marshaller.NameAwareMarshaller
import org.grails.web.converters.marshaller.ObjectMarshaller

/**
 * @since 27/10/2015
 */
class XmlMarshaller implements ObjectMarshaller<XML>, NameAwareMarshaller {
    @Override
    String getElementName(Object o) {
        ((XmlMarshallAware) o).xmlElementName()
    }

    @Override
    boolean supports(Object object) {
        object instanceof XmlMarshallAware
    }

    @Override
    void marshalObject(Object object, XML converter) throws ConverterException {
        ((XmlMarshallAware) object).marshallObject(converter)
    }
}

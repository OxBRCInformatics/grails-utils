package uk.ac.ox.ndm.grails.utils.marshalling

import grails.converters.JSON
import grails.converters.XML

import javax.annotation.PostConstruct
import javax.xml.datatype.DatatypeFactory
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * @since 22/09/2015
 */
class MarshallerRegistrar {

    @PostConstruct
    def registerJavaCoreMarshallers() {
        XML.registerObjectMarshaller(LocalDate) {
            it?.format(DateTimeFormatter.ISO_DATE)
        }

        XML.registerObjectMarshaller(OffsetDateTime) {
            it?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        XML.registerObjectMarshaller(UUID) {
            it?.toString()
        }

        XML.registerObjectMarshaller(Date) {Date dt ->
            GregorianCalendar calendar = new GregorianCalendar()
            calendar.setTime(dt)
            DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar).toString()
        }

        JSON.registerObjectMarshaller(LocalDate) {
            it?.toString()
        }

        JSON.registerObjectMarshaller(OffsetDateTime) {
            it?.toString()
        }

        JSON.registerObjectMarshaller(UUID) {
            it?.toString()
        }
    }

    @PostConstruct
    def registerMercuryMarshallers() {
        XML.registerObjectMarshaller(new XmlMarshaller())
    }
}

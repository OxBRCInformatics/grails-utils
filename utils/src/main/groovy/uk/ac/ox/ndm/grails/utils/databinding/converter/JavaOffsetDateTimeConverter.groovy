package uk.ac.ox.ndm.grails.utils.databinding.converter

import grails.databinding.converters.ValueConverter

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * @since 22/09/2015
 */
class JavaOffsetDateTimeConverter implements ValueConverter {
    @Override
    boolean canConvert(Object value) {
        value instanceof String
    }

    @Override
    Object convert(Object value) {
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @Override
    Class<?> getTargetType() {
        OffsetDateTime
    }
}

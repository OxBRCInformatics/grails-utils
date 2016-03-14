package uk.ac.ox.ndm.grails.utils.databinding.converter

import grails.databinding.converters.ValueConverter

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @since 02/09/2015
 */
class JavaLocalDateConverter implements ValueConverter {
    @Override
    boolean canConvert(Object value) {
        value instanceof String
    }

    @Override
    Object convert(Object value) {
        value ? LocalDate.parse(value, DateTimeFormatter.ISO_DATE) : null
    }

    @Override
    Class<?> getTargetType() {
        LocalDate
    }
}

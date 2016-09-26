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
        value instanceof String || value instanceof Map
    }

    @Override
    Object convert(Object value) {
        if (!value) return null
        if (value instanceof String)
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE)
        Map map = value as Map
        map.year ? LocalDate.of(map.year, map.month ?: 1, map.day ?: 1) : null
    }

    @Override
    Class<?> getTargetType() {
        LocalDate
    }
}

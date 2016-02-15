package uk.ac.ox.ndm.utils.grails.databinding.converter

import grails.databinding.converters.ValueConverter

import java.time.OffsetDateTime

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
        OffsetDateTime.parse(value)
    }

    @Override
    Class<?> getTargetType() {
        OffsetDateTime
    }
}

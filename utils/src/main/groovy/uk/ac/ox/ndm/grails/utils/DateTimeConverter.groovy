package uk.ac.ox.ndm.grails.utils

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * @since 19/08/2015
 */
class DateTimeConverter {

    // Private constructor as this is a static methods class only
    private DateTimeConverter() {
    }

    static LocalDate toLocalDate(Date date) {
        toDateTime(date).toLocalDate()
    }

    static OffsetDateTime toDateTime(Date date) {
        OffsetDateTime.ofInstant(date.toInstant(), ZoneId.of('UTC'))
    }

    static Date toDate(LocalDate localDate) {
        Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC))
    }

    static Date toDate(OffsetDateTime dateTime) {
        Date.from(dateTime.toInstant())
    }
}

package uk.ac.ox.ndm.grails.utils

import org.junit.Assert
import org.junit.Test

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * @since 19/08/2015
 */
class DateTimeConverterTest extends Assert {

    @Test
    void testToLocalDate() {

        Date date = Date.newInstance()
        LocalDate actual = DateTimeConverter.toLocalDate(date)
        assertEquals("Should be today", LocalDate.now(), actual)

        date = Date.parse('dd/MM/yyyy', '02/03/2010')
        actual = DateTimeConverter.toLocalDate(date)
        assertEquals("Should be 02/03/2010", LocalDate.of(2010, 03, 02), actual)
    }

    @Test
    void testToDateTime() {
        Date date = Date.newInstance()
        OffsetDateTime actual = DateTimeConverter.toDateTime(date)
        assertNotNull("Should be today", actual)

        date = Date.parse('dd/MM/yyyy HH:mm:ss.SSS', '02/03/2010 05:32:45.123')
        actual = DateTimeConverter.toDateTime(date)
        assertEquals("Should be 02/03/2010 05:32:45.123", OffsetDateTime.of(2010, 03, 02, 5, 32, 45, 123000000, ZoneOffset.UTC),
                     actual)
    }

    @Test
    void testToDate() {


        Date actual = DateTimeConverter.toDate(LocalDate.of(2010, 03, 02))

        assertEquals("Should be 02/03/2010", Date.parse('dd/MM/yyyy', '02/03/2010'), actual)

        actual = DateTimeConverter.toDate(OffsetDateTime.of(2010, 03, 02, 5, 32, 45, 123000000, ZoneOffset.UTC))

        assertEquals("Should be 02/03/2010", Date.parse('dd/MM/yyyy HH:mm:ss.SSS', '02/03/2010 05:32:45.123'), actual)

    }
}

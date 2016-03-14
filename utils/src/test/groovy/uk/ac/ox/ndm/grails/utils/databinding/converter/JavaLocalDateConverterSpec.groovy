package uk.ac.ox.ndm.grails.utils.databinding.converter

import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

/**
 * @since 14/03/2016
 */
class JavaLocalDateConverterSpec extends Specification {


    @Shared
    JavaLocalDateConverter converter = new JavaLocalDateConverter()

    void 'test can convert of date strings'() {

        expect:
        converter.canConvert('2016-02-03')

        and:
        converter.canConvert('2016-02-03Z')

    }

    void 'test conversion of date strings'() {

        expect:
        converter.convert('2016-02-03') == LocalDate.of(2016, 2, 3)

        and:
        converter.convert('2016-02-03Z') == LocalDate.of(2016, 2, 3)

    }
}

package uk.ac.ox.ndm.grails.utils.databinding.converter

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertNotNull;

/**
 * @since 18/04/2016
 */
public class JavaOffsetDateTimeConverterTest {

    JavaOffsetDateTimeConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new JavaOffsetDateTimeConverter();
    }

    @Test
    public void convertWithZ() {

        def result = converter.convert('2015-07-20T08:35:02Z')
        assertNotNull 'zoned time', result

        result = converter.convert('2015-07-20T08:35:02.0Z')
        assertNotNull 'zoned time with 0 ms', result

        result = converter.convert('2015-07-20T08:35:02.000Z')
        assertNotNull 'zoned time with 000 ms', result

        result = converter.convert('2015-07-20T08:35:02.1Z')
        assertNotNull 'zoned time with 1 ms', result

        result = converter.convert('2015-07-20T08:35:02.100Z')
        assertNotNull 'zoned time with 100 ms', result

        result = converter.convert('2015-07-20T08:35:02.123Z')
        assertNotNull 'zoned time with 1123 ms', result
    }

    @Test
    public void convertWith0100() {

        def result = converter.convert('2015-07-20T08:35:02+01:00')
        assertNotNull 'zoned time', result

        result = converter.convert('2015-07-20T08:35:02.0+01:00')
        assertNotNull 'zoned time with 0 ms', result

        result = converter.convert('2015-07-20T08:35:02.000+01:00')
        assertNotNull 'zoned time with 000 ms', result

        result = converter.convert('2015-07-20T08:35:02.1+01:00')
        assertNotNull 'zoned time with 1 ms', result

        result = converter.convert('2015-07-20T08:35:02.100+01:00')
        assertNotNull 'zoned time with 100 ms', result

        result = converter.convert('2015-07-20T08:35:02.123+01:00')
        assertNotNull 'zoned time with 1123 ms', result
    }

    @Test
    public void convertWithNoZone() {

        def result = converter.convert('2015-07-20T08:35:02')
        assertNotNull 'zoned time', result

        result = converter.convert('2015-07-20T08:35:02.0')
        assertNotNull 'zoned time with 0 ms', result

        result = converter.convert('2015-07-20T08:35:02.000')
        assertNotNull 'zoned time with 000 ms', result

        result = converter.convert('2015-07-20T08:35:02.1')
        assertNotNull 'zoned time with 1 ms', result

        result = converter.convert('2015-07-20T08:35:02.100')
        assertNotNull 'zoned time with 100 ms', result

        result = converter.convert('2015-07-20T08:35:02.123')
        assertNotNull 'zoned time with 1123 ms', result
    }

}
package uk.ac.ox.ndm.utils.domain

import grails.validation.Validateable
import groovy.transform.EqualsAndHashCode
import uk.ac.ox.ndm.utils.domain.awareness.DataTypeEnumAware

@EqualsAndHashCode
abstract class DataType<K, E extends Enum> implements DataTypeEnumAware<K, E>, Validateable {
    /*
        DataType() {
            this('??', 'Not Supplied')
        }
    */
    String label
    K id

    DataType(K id) {
        this(id, 'Not Supplied')
    }

    DataType(K id, String label) {
        this.id = id
        this.label = label
    }

    DataType(E value) {
        this(value.id, value.label)
    }

    static constraints = {
        label nullable: false
        id nullable: false
    }
}

package uk.ac.ox.ndm.grails.utils.domain.awareness

import uk.ac.ox.ndm.grails.utils.domain.DataTypeEnum
import uk.ac.ox.ndm.grails.utils.marshalling.XmlMarshallAware

/**
 * @since 24/08/2015
 */
trait DataTypeEnumAware<K, E extends DataTypeEnum> implements XmlMarshallAware {

    E findEnumValue() {
        getDataTypeEnum().values().find {it.id == id}
    }

    @Override
    public String toString() {
        return "${getId()} (${getLabel()})"
    }

    @Override
    void marshall() {
        chars id
    }

    abstract static Class<E> getDataTypeEnum()

    public abstract String getLabel()

    public abstract void setLabel(String label)

    public abstract K getId()

    public abstract void setId(K id)
}

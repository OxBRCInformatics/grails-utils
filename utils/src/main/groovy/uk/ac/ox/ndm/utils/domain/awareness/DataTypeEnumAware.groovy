package uk.ac.ox.ndm.utils.domain.awareness

import uk.ac.ox.ndm.utils.grails.marshalling.XmlMarshallAware

/**
 * @since 24/08/2015
 */
trait DataTypeEnumAware<K, E extends Enum> implements XmlMarshallAware {

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

    abstract String getLabel()

    abstract void setLabel(String label)

    abstract K getId()

    abstract void setId(K id)
}

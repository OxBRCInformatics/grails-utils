package uk.ac.ox.ndm.utils.domain

import org.apache.commons.lang.WordUtils

/**
 * @since 24/08/2015
 */
trait DataTypeEnum<K, V extends DataType> {

    K id
    String labelStr

    abstract V getDataTypeObject()

    abstract String name()

    @Override
    public String toString() {
        "$id ($label)"
    }

    String getLabel() {
        this.labelStr ? this.labelStr : WordUtils.capitalizeFully(name().replaceAll(/_/, ' '))
    }

    static List<String> getLabelList() {
        values().collect {it.label}
    }

}

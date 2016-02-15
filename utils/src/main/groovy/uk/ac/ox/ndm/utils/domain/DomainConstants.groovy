package uk.ac.ox.ndm.utils.domain;

/**
 * @since 14/08/2015
 */
public trait DomainConstants {

    String getCoreNamespace() {
        "http://mercury.ndm.ox.ac.uk/${getSchemaVersionString()}"
    }

    /**
     * Domain version constant. Will be used to determine the schema/namespace used in constructing domain classes. Should be
     * of the format
     * 'v\d+_\d+(_\d+)?'.
     *
     * @return String of the domain version
     */
    String getDomainVersionString() {
        "v${schemaVersionString.replaceAll(/\./, '_')}"
    }

    /**
     * Domain version constant. Will be used to determine the schema/namespace used in constructing domain classes. Should be
     * of the format
     * '\d+.\d+(.\d+)?'.
     *
     * @return String of the domain version
     */
    abstract String getSchemaVersionString();
}

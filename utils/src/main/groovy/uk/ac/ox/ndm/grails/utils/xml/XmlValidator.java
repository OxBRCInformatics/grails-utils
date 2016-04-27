package uk.ac.ox.ndm.grails.utils.xml;

import groovy.util.slurpersupport.GPathResult;

/**
 * @since 01/03/2016
 */
public interface XmlValidator {

    /**
     * Does this helper validate the provided XML
     *
     * @param xml XML to validate
     *
     * @return schema name of XSD xml validates against or null
     */
    String validatesXml(String referenceId, String xml);

    /**
     * Does this helper validate the provided XML
     *
     * @param xml XML to validate
     *
     * @return schema name of XSD xml validates against or null
     */
    String validatesXml(String referenceId, GPathResult xml);


}

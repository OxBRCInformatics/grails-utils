package uk.ac.ox.ndm.utils.grails.hibernate;

import java.util.Set;

/**
 * @since 19/01/2016
 */
public interface NamingStrategyHelper {

    Set<Class> getKnownEmbeddedDomains();
}

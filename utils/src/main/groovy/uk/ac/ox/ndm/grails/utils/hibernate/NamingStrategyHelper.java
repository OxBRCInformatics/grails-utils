package uk.ac.ox.ndm.grails.utils.hibernate;

import java.util.List;
import java.util.Set;

/**
 * @since 19/01/2016
 */
public interface NamingStrategyHelper {

    String adjustTableName(Class clazz, String tableName, String packageName);

    String canonicalNameToSchemaName(String canonicalName);

    String cleanColumnName(String columnName);

    boolean cleansColumnName(String columnName);

    List<String> getDataSources(String canonicalName);

    Set<Class> getKnownEmbeddedDomains();

    boolean handlesClass(Class clazz);

    boolean handlesClass(String canonicalName);
}

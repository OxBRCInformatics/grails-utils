package uk.ac.ox.ndm.utils.serializer;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.util.Map;

/**
 * @since 08/09/2015
 */
public abstract class JaxbSerializeObject {
    @Override
    public String toString() {
        Map props = DefaultGroovyMethods.getProperties(this);
        props.remove("class");
        props.remove("logger");
        return DefaultGroovyMethods.toMapString(props);
    }

}

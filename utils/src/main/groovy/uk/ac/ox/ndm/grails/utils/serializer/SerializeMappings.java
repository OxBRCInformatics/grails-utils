package uk.ac.ox.ndm.grails.utils.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 10/09/2015
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeMappings {

    String[] factoryMethodMappings() default {};

    /**
     * Provide name mappings of serialised_key:class_property
     *
     * @return String array of mappings
     */
    String[] nameMappings() default {};
}

package uk.ac.ox.ndm.grails.utils.databinding;

import java.util.Map;

/**
 * @since 19/01/2016
 */
public interface XmlDataBindingSourceCreatorHelper {

    Object checkDataBindingSourceMap(Map<String, ?> dataBindingSourceMap, Class bindingTargetType);
}

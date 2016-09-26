package uk.ac.ox.ndm.grails.utils.databinding;

import java.util.List;
import java.util.Map;

/**
 * @since 19/01/2016
 */
public interface DataBindingSourceCreatorHelper {

    Map convertBindingTargetTypeListToMap(List<Object> dataList, Class bindingTargetType);

    Boolean convertsBindingTargetTypeListsToMap(Class bindingTargetType);

    Object handleDataBindingSourceMap(Map<String, ?> dataBindingSourceMap, Class bindingTargetType);

    Boolean handlesBindingTargetTypeMaps(Class bindingTargetType);
}

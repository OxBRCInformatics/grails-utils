package uk.ac.ox.ndm.grails.utils.databinding

/**
 * @since 19/01/2016
 * @deprecated use {@link DataBindingSourceCreatorHelper} instead
 */
@Deprecated
public trait XmlDataBindingSourceCreatorHelper implements DataBindingSourceCreatorHelper {

    abstract Object checkDataBindingSourceMap(Map<String, ?> dataBindingSourceMap, Class bindingTargetType);

    @Override
    Object handleDataBindingSourceMap(Map<String, ?> dataBindingSourceMap, Class bindingTargetType) {
        checkDataBindingSourceMap(dataBindingSourceMap, bindingTargetType)
    }

    @Override
    Boolean convertsBindingTargetTypeListsToMap(Class bindingTargetType) {
        return false
    }

    @Override
    Map convertBindingTargetTypeListToMap(List<Object> dataList, Class bindingTargetType) {
        return null
    }
}

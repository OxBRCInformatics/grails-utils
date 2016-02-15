package uk.ac.ox.ndm.grails.utils.databinding.bindingsource

import grails.databinding.DataBindingSource

/**
 * @since 08/10/2015
 */
abstract class AbstractDomainDataBindingSource<T> implements DataBindingSource {

    boolean dataSourceAware = true
    private T domain
    Class bindingTargetType


    AbstractDomainDataBindingSource(Map map, Class<T> bindingTargetType) {
        this.bindingTargetType = bindingTargetType
        domain = createDomain(map)
    }

    AbstractDomainDataBindingSource(Map map) {
        this(map, null)
    }

    abstract T createDomain(Map map);

    @Override
    Set<String> getPropertyNames() {
        domain.properties.keySet() as Set<String>
    }

    @Override
    Object getPropertyValue(String propertyName) {
        domain."$propertyName"
    }

    @Override
    Object getAt(String propertyName) {
        getPropertyValue propertyName
    }

    @Override
    boolean containsProperty(String propertyName) {
        propertyName in propertyNames
    }

    @Override
    boolean hasIdentifier() {
        domain.id ? true : false
    }

    @Override
    Object getIdentifierValue() {
        domain.id
    }

    @Override
    int size() {
        propertyNames.size()
    }

    T getDomain() {
        return domain
    }
}

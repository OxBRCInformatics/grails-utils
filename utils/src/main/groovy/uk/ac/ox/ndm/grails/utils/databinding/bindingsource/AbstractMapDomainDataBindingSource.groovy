package uk.ac.ox.ndm.grails.utils.databinding.bindingsource

import grails.databinding.DataBindingSource

/**
 * @since 08/10/2015
 */
abstract class AbstractMapDomainDataBindingSource implements DataBindingSource {

    protected Map map
    boolean dataSourceAware = true

    AbstractMapDomainDataBindingSource(Map map) {
        this.map = createDomainMap(map)
    }

    abstract Map createDomainMap(Map map);

    Set<String> getPropertyNames() {
        map.keySet()
    }

    Object getPropertyValue(String propertyName) {
        map.get propertyName
    }

    Object getAt(String propertyName) {
        getPropertyValue propertyName
    }

    boolean containsProperty(String propertyName) {
        map.containsKey propertyName
    }

    boolean hasIdentifier() {
        map.containsKey('id')
    }

    def getIdentifierValue() {
        map['id']
    }

    int size() {
        map.size()
    }

    def propertyMissing(String name) {
        map.get(name)
    }

    def propertyMissing(String name, def arg) {
        map.put(name, arg)
    }

    def methodMissing(String name, def args) {
        map."$name"(args)
    }
}

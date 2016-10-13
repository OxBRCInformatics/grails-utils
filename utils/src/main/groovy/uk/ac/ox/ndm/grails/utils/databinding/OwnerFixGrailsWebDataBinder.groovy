package uk.ac.ox.ndm.grails.utils.databinding

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.databinding.DataBindingSource
import grails.databinding.SimpleMapDataBindingSource
import grails.databinding.converters.ValueConverter
import grails.databinding.events.DataBindingListener
import grails.web.databinding.GrailsWebDataBinder
import groovy.transform.CompileStatic
import uk.ac.ox.ndm.grails.utils.databinding.bindingsource.AbstractMapDomainDataBindingSource

/**
 * @since 02/10/2015
 */
@CompileStatic
class OwnerFixGrailsWebDataBinder extends GrailsWebDataBinder {

    OwnerFixGrailsWebDataBinder(GrailsApplication grailsApplication) {
        super(grailsApplication)
    }

    @Override
    protected Object processProperty(Object obj, MetaProperty metaProperty, Object val, DataBindingSource source,
                                     DataBindingListener listener,
                                     Object errors) {

        if (source.dataSourceAware && (val instanceof Map || val instanceof AbstractMapDomainDataBindingSource)) {
            if (grailsApplication != null) {
                def domainClass = (GrailsDomainClass) grailsApplication.getArtefact('Domain', obj.getClass().name)
                if (domainClass != null) {
                    def property = domainClass.getPersistentProperty metaProperty.name
                    if (property != null && property.isBidirectional()) {
                        def otherSide = property.otherSide
                        if (otherSide.isOneToOne()) {
                            val[otherSide.name] = obj
                        }
                    }
                }
            }
        }


        return super.processProperty(obj, metaProperty, val, source, listener, errors)
    }

    @Override
    protected Object convert(Class typeToConvertTo, Object value) {
        if (value == null || typeToConvertTo.isAssignableFrom(value?.getClass())) {
            return value
        }
        if (conversionHelpers.containsKey(typeToConvertTo)) {
            ValueConverter converter = getConverter(typeToConvertTo, value)
            if (converter) {
                return converter.convert(value)
            }
        }
        if (conversionService?.canConvert(value.getClass(), typeToConvertTo)) {
            return conversionService.convert(value, typeToConvertTo)
        }
        if (Collection.isAssignableFrom(typeToConvertTo) && value instanceof String[]) {
            if (Set == typeToConvertTo) {
                return value as Set
            }
            if (List == typeToConvertTo) {
                return value as List
            }
        }
        else if (typeToConvertTo.isPrimitive() || typeToConvertTo.isArray()) {
            return value
        }
        else if (value instanceof Map) {
            def obj = typeToConvertTo.newInstance()
            bind obj, new SimpleMapDataBindingSource(value)
            return obj
        }
        else if (Enum.isAssignableFrom(typeToConvertTo) && value instanceof String) {
            return convertStringToEnum(typeToConvertTo, value)
        }
        else if (value instanceof DataBindingSource) {
            def obj = typeToConvertTo.newInstance()
            bind obj, value as DataBindingSource
            return obj
        }
        typeToConvertTo.newInstance value
    }
}

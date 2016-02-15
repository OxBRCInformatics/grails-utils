package uk.ac.ox.ndm.utils.grails.databinding

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.databinding.DataBindingSource
import grails.databinding.events.DataBindingListener
import grails.web.databinding.GrailsWebDataBinder
import groovy.transform.CompileStatic

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

        if (source.dataSourceAware && val instanceof Map) {
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
}

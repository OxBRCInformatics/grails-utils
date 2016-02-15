package uk.ac.ox.ndm.grails.utils.renderer

import uk.ac.ox.ndm.grails.utils.serializer.AbstractObjectFactory

/**
 * @since 10/09/2015
 */
trait JaxbObjectFactoryAware<O extends AbstractObjectFactory> {

    abstract O obtainObjectFactory();

}
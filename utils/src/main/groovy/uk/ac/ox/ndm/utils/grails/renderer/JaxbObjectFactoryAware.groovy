package uk.ac.ox.ndm.utils.grails.renderer

import uk.ac.ox.ndm.utils.serializer.AbstractObjectFactory

/**
 * @since 10/09/2015
 */
trait JaxbObjectFactoryAware<O extends AbstractObjectFactory> {

    abstract O obtainObjectFactory();

}
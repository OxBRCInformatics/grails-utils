package uk.ac.ox.ndm.grails.utils.renderer

import grails.rest.render.ContainerRenderer
import grails.rest.render.RenderContext
import uk.ac.ox.ndm.grails.utils.serializer.AbstractObjectFactory

/**
 * @since 02/09/2015
 */
abstract class JaxbCollectionXmlRenderer<C extends Object, O extends AbstractObjectFactory> extends JaxbXmlRenderer<Collection, O>
        implements
                ContainerRenderer<C, Collection> {

    final Class componentType

    JaxbCollectionXmlRenderer(Class<C> componentType) {
        super(Collection)
        this.componentType = componentType
    }

    @Override
    protected void renderXml(Collection object, RenderContext context) {

        if (jaxbSerializer.hasXmlSerialization()) {
            String elementName = getCollectionElementName(object)
            context.writer.write("<$elementName>\n")

            object.each {entry ->
                super.renderXml(entry, context)
                context.writer.write("\n")
            }
            context.writer.write("</$elementName>\n")
        }
        else {
            super.renderXml(object, context)
        }
    }

    static String getCollectionElementName(Collection object) {
        if (object instanceof List) return 'list'
        if (object instanceof Set) return 'set'
        return 'collection'
    }
}

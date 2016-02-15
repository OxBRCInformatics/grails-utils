package uk.ac.ox.ndm.utils.grails.renderer

import grails.rest.render.RenderContext
import org.eclipse.persistence.jaxb.metadata.MetadataSource
import org.eclipse.persistence.jaxb.metadata.XMLMetadataSource
import org.grails.plugins.web.rest.render.xml.DefaultXmlRenderer
import uk.ac.ox.ndm.utils.serializer.AbstractObjectFactory
import uk.ac.ox.ndm.utils.serializer.JaxbSerializable
import uk.ac.ox.ndm.utils.serializer.JaxbSerializer

/**
 * @since 01/09/2015
 */
abstract class JaxbXmlRenderer<T extends Object, O extends AbstractObjectFactory> extends DefaultXmlRenderer<T>
        implements JaxbObjectFactoryAware<O> {

    protected JaxbSerializer jaxbSerializer

    JaxbXmlRenderer(Class<T> targetType) {
        super(targetType)
        jaxbSerializer = initialiseJaxbSerializer()
    }

    @Override
    protected void renderXml(T object, RenderContext context) {
        renderXml(object, context.writer) ?: super.renderXml(object, context)
    }

    public boolean renderXml(T object, Writer writer) {
        if (jaxbSerializer.hasXmlSerialization()) {
            if (object instanceof JaxbSerializable) //TODO
                jaxbSerializer.marshalXml(object.obtainJaxbObject(), writer)
            else jaxbSerializer.marshalXml(object, writer)
            return true
        }
        return false
    }

    private JaxbSerializer initialiseJaxbSerializer() {
        List<MetadataSource> metadataSourceList = new ArrayList<>();
        return JaxbSerializer.getJaxbSerializer(metadataSourceList, obtainObjectFactoryClass());
    }

    public Class<O> obtainObjectFactoryClass() {
        return obtainObjectFactory().getClass();
    }

    private List<MetadataSource> addMetadataSource(List<MetadataSource> metadataSourceList, String filename) {
        InputStream bindingStream = JaxbSerializer.class.getClassLoader()
                .getResourceAsStream(filename);
        if (bindingStream != null) {
            metadataSourceList.add(new XMLMetadataSource(bindingStream));
        }
        else throw new Exception("No binding stream")
        return metadataSourceList;
    }
}

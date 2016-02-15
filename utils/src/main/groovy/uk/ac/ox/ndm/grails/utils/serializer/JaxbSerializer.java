package uk.ac.ox.ndm.grails.utils.serializer;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.metadata.MetadataSource;
import org.eclipse.persistence.oxm.MediaType;
import org.eclipse.persistence.oxm.NamespacePrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @since 23/07/2015
 */
public class JaxbSerializer {

    public static final Map<String, JaxbSerializer> jaxbSerializers = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(JaxbSerializer.class);
    private JAXBContext context;
    private Marshaller xmlMarshaller, jsonMarshaller;

    private JaxbSerializer(NamespacePrefixMapper namespacePrefixMapper, List<MetadataSource> metadataSourceList,
                           Class... classes) {
        String name = "";
        for (Class aClass : classes) {
            name += aClass.getCanonicalName() + " ";
        }
        logger.debug("Initialising JAXB Serialiser for {}", name);
        Map<String, Object> props = new HashMap<>();
        props.put(JAXBContextProperties.OXM_METADATA_SOURCE, metadataSourceList);
        try {
            context = JAXBContext.newInstance(classes, props);
        } catch (JAXBException ex) {
            logger.warn("JAXB context for {} unavailable due to: {}", Arrays.asList(classes),
                        ex.getMessage() == null ? ex.getLinkedException() : ex
                                .getMessage());
        }

        if (context != null) {
            logger.trace("Using JAXB context implementation {}", context.getClass());

            try {
                jsonMarshaller = makeMarshaller();
                jsonMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
            } catch (JAXBException ex) {
                logger.warn("JAXB JSON serialization for {} not available due to: {}", Arrays.asList(classes),
                            ex.getMessage() == null ? ex.getLinkedException() :
                            ex);
            }

            try {
                xmlMarshaller = makeMarshaller();
                xmlMarshaller.setProperty(MarshallerProperties.INDENT_STRING, "  ");
                if (namespacePrefixMapper != null)
                    xmlMarshaller.setProperty(MarshallerProperties.NAMESPACE_PREFIX_MAPPER, namespacePrefixMapper);
                xmlMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_XML);
                xmlMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
            } catch (JAXBException ex) {
                logger.warn("JAXB XML serialization for {} not available due to: {}", Arrays.asList(classes),
                            ex.getMessage() == null ? ex.getLinkedException() : ex);
            }

        } else {
            logger.warn("JAXB XML & JSON serialization for {} not available due to no available context", Arrays.asList(classes));
        }
    }

    public static JaxbSerializer getJaxbSerializer(NamespacePrefixMapper namespacePrefixMapper, Class... classes) {
        return getJaxbSerializer(namespacePrefixMapper, Collections.emptyList(), classes);
    }

    public static JaxbSerializer getJaxbSerializer(NamespacePrefixMapper namespacePrefixMapper,
                                                   List<MetadataSource> metadataSourceList,
                                                   Class... classes) {
        JaxbSerializer jaxbSerializer = new JaxbSerializer(namespacePrefixMapper, metadataSourceList, classes);
        for (Class aClass : classes) {
            jaxbSerializers.put(aClass.getCanonicalName(), jaxbSerializer);
        }
        return jaxbSerializer;
    }

    public static JaxbSerializer getJaxbSerializer(Class... classes) {
        return getJaxbSerializer(null, Collections.emptyList(), classes);
    }

    public static JaxbSerializer getJaxbSerializer(List<MetadataSource> metadataSourceList, Class... classes) {
        return getJaxbSerializer(null, metadataSourceList, classes);
    }

    public Marshaller getJsonMarshaller() {
        return jsonMarshaller;
    }

    public Marshaller getXmlMarshaller() {
        return xmlMarshaller;
    }

    public boolean hasJsonSerialization() {
        return jsonMarshaller != null;
    }

    public boolean hasXmlSerialization() {
        return xmlMarshaller != null;
    }

    public String marshalJson(Object jaxbElement) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        marshalJson(jaxbElement, stringWriter);
        return stringWriter.toString();
    }

    public void marshalJson(Object jaxbElement, Writer writer) throws JAXBException {
        getJsonMarshaller().marshal(jaxbElement, writer);
    }

    public void marshalXml(Object jaxbElement, Writer writer) throws JAXBException {
        getXmlMarshaller().marshal(jaxbElement, writer);
    }

    public String marshalXml(Object jaxbElement) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        marshalXml(jaxbElement, stringWriter);
        return stringWriter.toString();
    }

    private Marshaller makeMarshaller() throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }
}

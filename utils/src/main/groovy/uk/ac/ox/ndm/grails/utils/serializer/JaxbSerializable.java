package uk.ac.ox.ndm.grails.utils.serializer;


import uk.ac.ox.ndm.grails.utils.renderer.JaxbObjectFactoryAware;

/**
 * @since 07/09/2015
 */
public abstract class JaxbSerializable<C extends JaxbSerializeObject, O extends AbstractObjectFactory>
        implements JaxbObjectFactoryAware<O> {

    protected boolean jaxbGenerated;
    protected C jaxbObjectHolder;

    public JaxbSerializable(C jaxbObject) {
        this.jaxbObjectHolder = jaxbObject;
        jaxbGenerated = false;
    }

    public C obtainJaxbObject() {
        if (!jaxbGenerated) {
            obtainObjectFactory().buildModel(this, jaxbObjectHolder);
            jaxbGenerated = true;
        }
        return jaxbObjectHolder;
    }
}

package uk.ac.ox.ndm.grails.utils.xml.resource;

import org.w3c.dom.ls.LSInput;

import java.io.InputStream;
import java.io.Reader;

/**
 * @since 01/03/2016
 */
public class ResourceInput implements LSInput {

    private String baseURI;
    private InputStream byteStream;
    private boolean certifiedText = false;
    private Reader characterStream;
    private String encoding;
    private String publicId;
    private String stringData;
    private String systemId;

    public Reader getCharacterStream() {
        return characterStream;
    }

    public void setCharacterStream(Reader characterStream) {
        this.characterStream = characterStream;
    }

    public InputStream getByteStream() {
        return byteStream;
    }

    public void setByteStream(InputStream byteStream) {
        this.byteStream = byteStream;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean getCertifiedText() {
        return certifiedText;
    }

    public boolean isCertifiedText() {
        return certifiedText;
    }

    public void setCertifiedText(boolean certifiedText) {
        this.certifiedText = certifiedText;
    }
}

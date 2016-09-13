package uk.ac.ox.ndm.grails.utils.databinding

import grails.databinding.CollectionDataBindingSource
import grails.databinding.DataBindingSource
import grails.web.mime.MimeType
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.transform.CompileStatic
import groovy.util.slurpersupport.GPathResult
import org.grails.databinding.bindingsource.DataBindingSourceCreationException
import org.grails.databinding.xml.GPathResultCollectionDataBindingSource
import org.grails.databinding.xml.GPathResultMap
import org.grails.io.support.SpringIOUtils
import org.grails.web.databinding.bindingsource.InvalidRequestBodyException
import org.xml.sax.SAXParseException

import javax.servlet.http.HttpServletRequest

/**
 * @since 02/09/2015
 */
@CompileStatic
class AdvancedXmlDataBindingSourceCreator extends AdvancedDataBindingSourceCreator {

    @Override
    MimeType[] getMimeTypes() {
        [MimeType.XML, MimeType.TEXT_XML] as MimeType[]
    }

    @Override
    CollectionDataBindingSource createCollectionDataBindingSource(MimeType mimeType, Class bindingTargetType,
                                                                  Object bindingSource) {
        if (bindingSource instanceof GPathResult) {
            new GPathResultCollectionDataBindingSource(bindingSource)
        }
        else {
            try {
                if (bindingSource instanceof GrailsParameterMap) {
                    def req = bindingSource.getRequest()
                    def is = req.getInputStream()
                    return createCollectionBindingSource(is, req.getCharacterEncoding())
                }
                if (bindingSource instanceof HttpServletRequest) {
                    def req = bindingSource as HttpServletRequest
                    def is = req.getInputStream()
                    return createCollectionBindingSource(is, req.getCharacterEncoding())
                }
                if (bindingSource instanceof InputStream) {
                    def is = bindingSource as InputStream
                    return createCollectionBindingSource(is, "UTF-8")
                }
                if (bindingSource instanceof Reader) {
                    def is = bindingSource as Reader
                    return createCollectionBindingSource(is)
                }

                return super.createCollectionDataBindingSource(mimeType, bindingTargetType, bindingSource)
            } catch (Exception e) {
                throw new DataBindingSourceCreationException(e)
            }
        }
    }

    CollectionDataBindingSource createCollectionBindingSource(InputStream inputStream, String charsetName) {
        createCollectionBindingSource(new InputStreamReader(inputStream, charsetName ?: 'UTF-8'))
    }

    CollectionDataBindingSource createCollectionBindingSource(Reader reader) {
        new GPathResultCollectionDataBindingSource(SpringIOUtils.createXmlSlurper().parse(reader))
    }

    // Single value data binding processing

    @Override
    DataBindingSource createDataBindingSource(MimeType mimeType, Class bindingTargetType, Object bindingSource) {
        try {
            if (bindingSource instanceof String) {
                GPathResult result = new XmlSlurper().parseText(new String(bindingSource))
                return createDataBindingSource(result, bindingTargetType)
            }
            if (bindingSource instanceof GPathResult) {
                return createDataBindingSource(bindingSource, bindingTargetType)
            }
            if (bindingSource instanceof HttpServletRequest) {
                def req = bindingSource as HttpServletRequest
                def is = req.getInputStream()
                return createDataBindingSource(is, req.getCharacterEncoding(), bindingTargetType)
            }
            if (bindingSource instanceof InputStream) {
                def is = bindingSource as InputStream
                return createDataBindingSource(is, "UTF-8", bindingTargetType)
            }
            if (bindingSource instanceof Reader) {
                def is = bindingSource as Reader
                return createDataBindingSource(is, bindingTargetType)
            }
            return super.createDataBindingSource(mimeType, bindingTargetType, bindingSource)
        } catch (Exception e) {
            throw createBindingSourceCreationException(e)
        }
    }

    DataBindingSource createDataBindingSource(Reader reader, Class bindingTargetType) {
        createDataBindingSource(SpringIOUtils.createXmlSlurper().parse(reader), bindingTargetType)
    }

    DataBindingSource createDataBindingSource(GPathResult gPathResult, Class bindingTargetType) {
        createDataBindingSource(new GPathResultMap(gPathResult), bindingTargetType)
    }

    DataBindingSourceCreationException createBindingSourceCreationException(Exception e) {
        if (e instanceof SAXParseException) {
            logger.error "Exception while creating databinding source: {}", e.message
            return new InvalidRequestBodyException(e)
        }
        return super.createBindingSourceCreationException(e)
    }
}

package uk.ac.ox.ndm.grails.utils.databinding

import grails.databinding.DataBindingSource
import grails.web.mime.MimeType
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.grails.databinding.bindingsource.DataBindingSourceCreationException
import org.grails.web.databinding.bindingsource.InvalidRequestBodyException
import org.grails.web.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired

/**
 * @since 13/09/2016
 */
@CompileStatic
class AdvancedJsonDataBindingSourceCreator extends AdvancedDataBindingSourceCreator {


    @Autowired(required = false)
    JsonSlurper jsonSlurper = new JsonSlurper()

    @Override
    MimeType[] getMimeTypes() {
        [MimeType.JSON, MimeType.TEXT_JSON] as MimeType[]
    }

    @Override
    DataBindingSource createDataBindingSource(MimeType mimeType, Class bindingTargetType, Object bindingSource) {

        if (bindingSource instanceof Map) {
            return createDataBindingSource(bindingSource, bindingTargetType)
        }
        else if (bindingSource instanceof JSONObject) {
            return createDataBindingSource(bindingSource, bindingTargetType)
        }
        else if (bindingSource instanceof Reader) {
            return createDataBindingSource(bindingSource, bindingTargetType)
        }
        else {
            return super.createDataBindingSource(mimeType, bindingTargetType, bindingSource)
        }
    }

    @Override
    DataBindingSource createDataBindingSource(Reader reader, Class bindingTargetType) {
        final def jsonElement = jsonSlurper.parse(reader)
        createDataBindingSource(jsonElement as JSONObject, bindingTargetType)
    }

    @Override
    DataBindingSourceCreationException createBindingSourceCreationException(Exception e) {
        if (e instanceof JsonException) {
            return new InvalidRequestBodyException(e)
        }
        return super.createBindingSourceCreationException(e)
    }
}

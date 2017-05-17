package uk.ac.ox.ndm.grails.utils.test

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovy.xml.XmlUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource

import javax.annotation.Resource

/**
 * @since 15/09/2015
 */
abstract class ControllerIntegSpec extends CoreSpec {

    @Resource
    MessageSource messageSource

    RestBuilder rest

    public RestResponse response

    @Value('${server.host:localhost}')
    String host

    boolean https = false

    String getBaseUrl() {
        "http${https ? 's' : ''}://${host}:${serverPort}"
    }

    String getAcceptVersion() {
        null
    }

    def setup() {
        logger.info "Setting up core integ spec"

        rest = new RestBuilder()
    }

    def get(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        get(relativeUrl, Collections.emptyMap(), customizer)
    }

    def get(String relativeUrl, Map<String, Object> urlVariables, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.get("${getBaseUrl()}/$relativeUrl", urlVariables) {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def post(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.post("${getBaseUrl()}/$relativeUrl") {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def put(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.put("${getBaseUrl()}/$relativeUrl") {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def delete(String relativeUrl, Map<String, Object> urlVariables, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.delete("${getBaseUrl()}/$relativeUrl", urlVariables) {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def cleanup() {
        if (response?.text) {
            try {
                if(response.xml?.errors?.size()) dump( 'error', true)
            } catch (Exception ignored) {
                dump('error')
            }
        }
        response = null
    }

    def dump(String level, boolean format = false) {
        String text = format ? XmlUtil.serialize(new XmlParser().parseText(response.text)) : response.text
        logger."$level" text
        if (level == 'error') System.err.println text
    }

    void setHost(String host) {
        this.host = host
    }
}

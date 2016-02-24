package uk.ac.ox.ndm.grails.utils.test

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovy.xml.XmlUtil
import org.springframework.context.MessageSource
import spock.lang.Shared

import javax.annotation.Resource

/**
 * @since 15/09/2015
 */
abstract class CoreIntegSpec extends CoreSpec {

    @Resource
    MessageSource messageSource

    @Shared
    RestBuilder rest
    @Shared
    RestResponse response
    @Shared
    String baseUrl = 'http://localhost:8080'

    String getAcceptVersion() {
        null
    }

    def setup() {
        logger.warn("--- ${specificationContext.currentIteration.name} ---")
        logger.info "Setting up core spec"

        rest = new RestBuilder()
    }

    def get(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        get(relativeUrl, Collections.emptyMap(), customizer)
    }

    def get(String relativeUrl, Map<String, Object> urlVariables, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.get("$baseUrl/$relativeUrl", urlVariables) {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def post(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.post("$baseUrl/$relativeUrl") {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def put(String relativeUrl, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.put("$baseUrl/$relativeUrl") {
            if (customizer) {
                customizer.delegate = delegate
                customizer.resolveStrategy = resolveStrategy
                customizer.call()
            }
            if (acceptVersion) header('Accept-Version', acceptVersion)
        }
    }

    def delete(String relativeUrl, Map<String, Object> urlVariables, @DelegatesTo(RequestCustomizer) Closure customizer = null) {
        response = rest.delete("$baseUrl/$relativeUrl", urlVariables) {
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
                dump(response.xml?.errors?.size() ? 'error' : 'warn', true)
            } catch (Exception ignored) {
                dump('error')
            }
        }
    }

    def dump(String level, boolean format = false) {
        String text = format ? XmlUtil.serialize(new XmlParser().parseText(response.text)) : response.text
        logger."$level" text
        if (level == 'error') System.err.println text
    }
}

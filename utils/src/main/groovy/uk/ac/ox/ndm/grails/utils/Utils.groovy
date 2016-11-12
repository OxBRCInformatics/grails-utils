package uk.ac.ox.ndm.grails.utils

import java.nio.charset.StandardCharsets


/**
 * @since 16/09/2015
 */
trait Utils {

    Map<String, Object> extractNameMappings(String[] nameMappings) {
        def mappings = [:]
        nameMappings?.each {content ->
            String[] keyValues = content.split(':')
            List<String> keys = keyValues[0].split(/\./).toList()
            List<String> values = keyValues[1].split(/\./).toList()
            mappings = buildMapping(mappings, keys, values)
        }
        mappings
    }

    def buildMapping(def mapping, List<String> keys, List<String> values) {

        if (keys) {
            def key = keys.remove(0)
            mapping = mapping ?: [:]
            if (mapping instanceof String) {
                mapping = ['-': mapping]
            }

            mapping[key] = buildMapping(mapping[key], keys, values)
        }
        else {
            if (!values) throw new IllegalStateException("Must have a value to assign")
            def value = values.remove(0)

            if (values) {
                if (values.size() == 1) {
                    def valMap = [:]
                    valMap[value] = values.first()
                    value = valMap
                }
                else throw new IllegalStateException("Cannot handle a double nested value mapping")
            }
            if (mapping) {
                if (mapping instanceof Map) {
                    mapping.'-' = value
                }
            }
            else {
                mapping = value
            }
        }


        mapping
    }

    String convertByteArrayToBase64EncodedString(Byte[] bytes) {
        bytes ? new String(Base64.encoder.encode(bytes), StandardCharsets.UTF_8) : null
    }

    Byte[] convertBase64EncodedStringToByteArray(String encoded) {
        encoded ? Base64.decoder.decode(encoded) : null
    }

}

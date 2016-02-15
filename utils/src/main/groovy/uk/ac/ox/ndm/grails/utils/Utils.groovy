package uk.ac.ox.ndm.grails.utils

import java.nio.charset.StandardCharsets


/**
 * @since 16/09/2015
 */
trait Utils {

    Map<String, Object> extractNameMappings(String[] nameMappings) {
        def mappings = [:]
        nameMappings?.each {content ->
            String[] split = content.split(/\./)
            buildMap(mappings, split)
        }
        mappings
    }

    def buildMap(Map<String, Object> map, String[] contents) {
        if (contents) {
            String entry = contents[0]
            String[] remainder = contents - entry

            if (entry.contains(':')) {
                if (remainder)
                    throw new IllegalStateException('Cannot have key:value and contents')

                def kv = entry.split(':')

                if (map."${kv[0]}" instanceof Map) map."${kv[0]}".'-' = kv[1]
                else map."${kv[0]}" = kv[1]
            }
            else {

                def submap = map."$entry" ?: [:]
                if (submap instanceof String)
                    submap = ['-': submap]
                submap = buildMap(submap, remainder)
                map."$entry" = submap

            }
        }

        map
    }

    String convertByteArrayToBase64EncodedString(Byte[] bytes) {
        bytes ? new String(Base64.encoder.encode(bytes), StandardCharsets.UTF_8) : null
    }

    Byte[] convertBase64EncodedStringToByteArray(String encoded) {
        encoded ? Base64.decoder.decode(encoded) : null
    }

}

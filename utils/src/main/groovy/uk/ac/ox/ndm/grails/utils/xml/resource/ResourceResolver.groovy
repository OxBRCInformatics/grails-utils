package uk.ac.ox.ndm.grails.utils.xml.resource

import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.fs.AssetResolver
import org.w3c.dom.ls.LSInput
import org.w3c.dom.ls.LSResourceResolver

/**
 * @since 01/03/2016
 */
class ResourceResolver implements LSResourceResolver {

    final Map<String, String> schemaCache

    ResourceResolver(String assetFilename) {
        AssetResolver resolver = AssetPipelineConfigHolder.resolvers.find {it.getAsset(assetFilename)}
        schemaCache = resolver.scanForFiles([], ['*.xsd']).collectEntries {[it.name, new String(it.inputStream.bytes)]}
    }

    @Override
    LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        new ResourceInput(characterStream: new StringReader(schemaCache.get(systemId)), systemId: systemId, baseURI: baseURI)
    }
}
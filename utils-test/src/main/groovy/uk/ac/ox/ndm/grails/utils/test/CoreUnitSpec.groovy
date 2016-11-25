package uk.ac.ox.ndm.grails.utils.test

import grails.util.Holders
import org.grails.config.PropertySourcesConfig

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @since 15/09/2015
 */
abstract class CoreUnitSpec extends CoreSpec {

    static doWithConfig(PropertySourcesConfig config) {

        Path appFile = getGrailsDirectory(config).resolve('grails-app/conf/application.groovy')
        if (!Files.exists(appFile)) return

        URL applicationGroovyUrl = appFile.toUri().toURL()
        if (!applicationGroovyUrl) throw new IllegalStateException('We need the application groovy config to be able to test')
        config.merge(new ConfigSlurper().parse(applicationGroovyUrl))
    }

    static doWithSpring = {
        def config = Holders.findApplication().config

        Path resourcesFile = getGrailsDirectory(config).resolve('grails-app/conf/spring/resources.groovy')
        if (!Files.exists(resourcesFile)) return

        URL resourcesGroovyUrl = resourcesFile.toUri().toURL()
        if (!resourcesGroovyUrl) throw new IllegalStateException('We need the spring resources groovy config to be able to test')
        def spring = new ConfigSlurper().parse(resourcesGroovyUrl)
        spring.beans.dehydrate().rehydrate(delegate, owner, thisObject).run()
    }

    static Path getGrailsDirectory(def config){
        Path userDir = Paths.get(config.'user.dir' as String)
        if(config.'grails.project.base.dir' as String) {
            Path projectDir = Paths.get(config.'grails.project.base.dir' as String)
            if (projectDir) {
                userDir = (projectDir.fileName == userDir.fileName) ? userDir : userDir.resolve(projectDir)
            }
        }
        userDir
    }

    def setup() {
        logger.info "Setting up core unit spec"

        registerConstraints()

        mockDomains(getKnownDataTypes().core)

        loadI18nMessages()
    }

    def loadI18nMessages() {

        def config = Holders.findApplication().config

        Path messagesFile = getGrailsDirectory(config).resolve('grails-app/i18n/messages.properties')
        if (!Files.exists(messagesFile)) return

        Properties messages = new Properties()
        messages.load(new FileReader(messagesFile.toFile()))
        messages.stringPropertyNames().each {k ->
            addMessageCode(k, messages.getProperty(k))
        }
    }
}

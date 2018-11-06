package uk.ac.ox.ndm.grails.utils.test

import grails.testing.gorm.DataTest
import grails.util.Holders

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @since 15/09/2015
 */
abstract class CoreUnitSpec extends DomainSpec implements DataTest {

    static Path workingDirectory

    static Path getGrailsDirectory(def config) {
        workingDirectory = Paths.get(config.'user.dir' as String)
        if (config.'grails.project.base.dir' as String) {
            Path projectDir = Paths.get(config.'grails.project.base.dir' as String)
            if (projectDir) {
                workingDirectory = (projectDir.fileName == workingDirectory.fileName) ? workingDirectory : workingDirectory.resolve(projectDir)
            }
        }
        workingDirectory
    }

    def setup() {
        logger.info "Setting up core unit spec"

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

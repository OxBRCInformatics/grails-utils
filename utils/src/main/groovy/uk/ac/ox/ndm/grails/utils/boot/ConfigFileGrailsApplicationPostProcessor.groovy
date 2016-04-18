package uk.ac.ox.ndm.grails.utils.boot

import grails.boot.config.GrailsApplicationPostProcessor
import grails.core.DefaultGrailsApplication
import grails.core.GrailsApplicationLifeCycle
import org.grails.config.PropertySourcesConfig
import org.grails.config.yaml.YamlPropertySourceLoader
import org.grails.core.cfg.GroovyConfigPropertySourceLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.env.PropertiesPropertySourceLoader
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.context.ApplicationContext
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertySource
import org.springframework.core.env.SimpleCommandLinePropertySource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @since 18/04/2016
 */
public class ConfigFileGrailsApplicationPostProcessor extends GrailsApplicationPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConfigFileGrailsApplicationPostProcessor)
    public static final String COMMAND_LINE_ARGS_PROPERTY_SOURCE = 'commandLineArgs'
    public static final String CONFIG_FILE_PROPERTY_SOURCE = 'additionalConfigurationFile'

    ConfigFileGrailsApplicationPostProcessor(GrailsApplicationLifeCycle lifeCycle, ApplicationContext applicationContext, Class... classes) {
        super(lifeCycle, applicationContext, classes);
    }

    @Override
    protected void loadApplicationConfig() {
        super.loadApplicationConfig();

        PropertySourcesConfig config = ((DefaultGrailsApplication) grailsApplication).config
        MutablePropertySources propertySources = config.getPropertySources()
        SimpleCommandLinePropertySource commandLine = propertySources.get(COMMAND_LINE_ARGS_PROPERTY_SOURCE)

        if (commandLine.containsProperty('configFile') || commandLine.containsProperty('c')) {

            Path path = Paths.get(commandLine.getProperty('configFile') ?: commandLine.getProperty('c'))

            if (Files.exists(path)) {

                String ext = com.google.common.io.Files.getFileExtension(path.fileName);
                Resource resource = new FileSystemResource(path.toFile())
                PropertySourceLoader propertySourceLoader
                try {
                    switch (ext) {
                        case 'yml': case 'yaml':
                            propertySourceLoader = new YamlPropertySourceLoader()
                            break
                        case 'groovy':
                            propertySourceLoader = new GroovyConfigPropertySourceLoader()
                            break
                        case 'properties': case 'xml':
                            propertySourceLoader = new PropertiesPropertySourceLoader()
                            break
                        default:
                            logger.warn("Specified configuration file {} is not one of ['yml','yaml','groovy','properties','xml']", path)
                    }

                    if (propertySourceLoader) {
                        PropertySource<?> propertySource = propertySourceLoader.load(CONFIG_FILE_PROPERTY_SOURCE, resource, null)
                        propertySources.addAfter COMMAND_LINE_ARGS_PROPERTY_SOURCE, propertySource
                        config.refresh()
                        logger.info('Updated property sources to include properties from configuration file {}', path)
                    }

                } catch (IOException e) {
                    logger.warn("Error loading configuration file '$path': ${e.getMessage()}", e);
                }
            }
            else
                logger.warn("Specified configuration file {} cannot be found", path)
        }

    }
}

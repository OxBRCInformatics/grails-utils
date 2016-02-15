package uk.ac.ox.ndm.grails.utils.test

import grails.util.Holders
import org.grails.config.PropertySourcesConfig

/**
 * @since 15/09/2015
 */
abstract class CoreUnitSpec extends CoreSpec {

    static doWithConfig(PropertySourcesConfig config) {

        File appFile = new File(config."user.dir" as String, (config.'grails.project.base.dir' as String) +
                                                             '/grails-app/conf/application.groovy')
        if (!appFile.exists()) throw new IllegalStateException('We need the application groovy config to be able to test')

        URL applicationGroovyUrl = appFile.toURI().toURL()
        if (!applicationGroovyUrl) throw new IllegalStateException('We need the application groovy config to be able to test')
        config.merge(new ConfigSlurper().parse(applicationGroovyUrl))
    }

    static doWithSpring = {
        def config = Holders.findApplication().config

        File resourcesFile = new File(config."user.dir" as String, (config.'grails.project.base.dir' as String) +
                                                                   '/grails-app/conf/spring/resources.groovy')
        if (!resourcesFile.exists()) throw new IllegalStateException('We need the application groovy config to be able to test')

        URL resourcesGroovyUrl = resourcesFile.toURI().toURL()
        if (!resourcesGroovyUrl) throw new IllegalStateException('We need the spring resources groovy config to be able to test')
        def spring = new ConfigSlurper().parse(resourcesGroovyUrl)
        spring.beans.dehydrate().rehydrate(delegate, owner, thisObject).run()
    }

    def setup() {
        logger.warn("--- ${specificationContext.currentIteration.name} ---")
        logger.info "Setting up core spec"

        registerConstraints()

        mockDomains(getKnownDataTypes().core)

        addMessageCode('validation.choice.onlyone', 'Property {0} with value {2} is invalid, only one {3} may be set')
        addMessageCode('validation.choice.atleastone', 'Property {0} with value {2} is invalid, at least one {3} must be set')
        addMessageCode('validation.nhsnumber.wronglength',
                       'Property {0} with value {2} is invalid, it must be at least 10 digits for a valid NHS ' +
                       'number')
        addMessageCode('validation.empty', 'Property {0} is invalid, it cannot be empty')
        addMessageCode('validation.hasmany.size.atleast', 'Set must have at least {3} element(s)')
        addMessageCode('validation.hasmany.size.atmost', 'Set cannot have more than {3} element(s)')
        addMessageCode('validation.schema.version', "Schema {3} with version value {2} must be one of {4}")
        addMessageCode('default.not.unique', 'Property [{0}] of class [{1}] with value [{2}] must be unique')
    }
}

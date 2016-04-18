package uk.ac.ox.ndm.grails.utils.boot

import grails.boot.config.GrailsApplicationPostProcessor
import grails.boot.config.GrailsAutoConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

/**
 * @since 18/04/2016
 */
class ConfigFileGrailsAutoConfiguration extends GrailsAutoConfiguration {

    @Override
    GrailsApplicationPostProcessor grailsApplicationPostProcessor() {
        return new ConfigFileGrailsApplicationPostProcessor(this, applicationContext, classes() as Class[])
    }

    public static void outputRuntimeArgs() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        Logger logger = LoggerFactory.getLogger(getClass())
        logger.info("Running with JVM args: " + arguments.join(', '))
    }
}

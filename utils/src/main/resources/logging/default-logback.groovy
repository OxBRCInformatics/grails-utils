package logging

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import grails.util.BuildSettings
import grails.util.Environment

String defPattern = '%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n'

File baseDir = BuildSettings.BASE_DIR.canonicalFile
File targetDir = BuildSettings.TARGET_DIR.canonicalFile

def logDir = new File(targetDir, 'logs')
if (!logDir) logDir.mkdirs()

appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = defPattern
    }
    if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
        filter(ThresholdFilter) {
            level = WARN
        }
    }
    else {
        filter(ThresholdFilter) {
            level = ERROR
        }
    }
}

appender("FILE", FileAppender) {
    file = "${logDir}/mercury_${baseDir.name.toLowerCase()}.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = defPattern
    }
}

root(WARN, ['STDOUT', 'FILE'])

if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {

    logger('org.grails.orm.hibernate.cfg.HibernateMappingBuilder', OFF)
    logger('org.hibernate.tool.hbm2ddl.SchemaExport', OFF)
    logger('uk.ac.ox', DEBUG)
    logger('org.hibernate.SQL', DEBUG)
    //  logger 'org.hibernate.type', TRACE
    logger('org.grails.spring.beans.factory.OptimizedAutowireCapableBeanFactory', ERROR)

}

//logger 'liquibase-hibernate', ERROR
//logger 'org.grails.plugins.databasemigration', INFO
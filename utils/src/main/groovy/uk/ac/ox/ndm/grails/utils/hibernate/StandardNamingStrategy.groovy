package uk.ac.ox.ndm.grails.utils.hibernate

import grails.compiler.GrailsCompileStatic
import grails.core.GrailsClass
import grails.util.Holders
import org.grails.core.artefact.DomainClassArtefactHandler
import org.hibernate.cfg.ImprovedNamingStrategy
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.regex.Pattern

/**
 * @since 05/11/2015
 */
@GrailsCompileStatic
class StandardNamingStrategy extends ImprovedNamingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(StandardNamingStrategy)

    Pattern getDomainVersionPattern(){
        Pattern.compile(/(uk\.ac\.ox\.ndm)(\.\w+)*(\.endpoint)?\.(?<endpoint>\w+)\.(?<version>v\d+(_\d+){0,2})\.*/);
    }

    Pattern getPackagePattern(){
        Pattern.compile(/(uk\.ac\.ox\.ndm)(\.\w+)*(\.endpoint)?\.(?<endpoint>\w+)\.(?<package>\w+)\.*/)
    }

    public static final StandardNamingStrategy INSTANCE = new StandardNamingStrategy()

    @Override
    String classToTableName(String className) {
        Collection<GrailsClass> artefacts = Holders.grailsApplication.getArtefacts(DomainClassArtefactHandler.TYPE)
                .findAll {it.shortName == className}

        if (artefacts) {
            Class clazz = artefacts.first().clazz
            if (artefacts.size() > 1) {
                logger.warn("More than one artefact found for name {}, using first in list {}", className,
                            clazz.canonicalName)
            }
            return classToTableName(clazz)
        }
        logger.debug('No artefact matching name {}, using default class to table name', className)
        super.classToTableName(className)
    }

    String canonicalNameToTableName(String className) {
        Collection<GrailsClass> artefacts = Holders.grailsApplication.getArtefacts(DomainClassArtefactHandler.TYPE)
                .findAll {it.fullName == className}

        if (artefacts) {
            Class clazz = artefacts.first().clazz
            if (artefacts.size() > 1) {
                logger.warn("More than one artefact found for name {}, using first in list {}", className,
                            clazz.canonicalName)
            }
            return classToTableName(clazz)
        }

        logger.debug('No artefact matching name {}, trying short name', className)
        int i = className.lastIndexOf('.')
        classToTableName(className.substring(i + 1))
    }

    String classToTableName(Class clazz) {

        String tableName = super.classToTableName(clazz.simpleName)

        String packageName = clazz.package.name
        tableName = adjustTableName(clazz, tableName, packageName)
        handleDatatypes(packageName, tableName)
    }

    String handleDatatypes(String packageName, String tableName) {
        packageName.contains("datatype") ? "dt_$tableName" : tableName
    }

    String adjustTableName(Class clazz, String tableName, String packageName) {
        def matcher = getPackagePattern().matcher(packageName);
        if (!packageName.contains('datatype') && matcher.find()) {
            tableName = "${matcher.group('package')}_$tableName"
        }
        tableName
    }

    String classToSchemaName(Class clazz) {
        canonicalNameToSchemaName(clazz.canonicalName)
    }

    String canonicalNameToSchemaName(String canonicalName) {
        String schemaName = 'public'

        def matcher = getDomainVersionPattern().matcher(canonicalName);
        if (matcher.find()) {
            schemaName = matcher.group('version')
        }
        schemaName
    }
}

package uk.ac.ox.ndm.grails.utils.hibernate

import grails.compiler.GrailsCompileStatic
import grails.core.GrailsClass
import grails.util.Holders
import org.grails.core.artefact.DomainClassArtefactHandler
import org.hibernate.cfg.ImprovedNamingStrategy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import java.util.regex.Pattern

/**
 * @since 05/11/2015
 */
@GrailsCompileStatic
class StandardNamingStrategy extends ImprovedNamingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(StandardNamingStrategy)

    static final Pattern STANDARD_DOMAIN_VERSION_PATTERN =
            Pattern.compile(/(uk\.ac\.ox\.ndm)(\.\w+)*(\.endpoint)?\.(?<endpoint>\w+)\.(?<version>v\d+(_\d+){0,2})\.*/);

    static final Pattern STANDARD_PACKAGE_PATTERN =
            Pattern.compile(/(uk\.ac\.ox\.ndm)(\.\w+)*(\.endpoint)?\.(?<endpoint>\w+)\.(?<package>\w+)\.*/)

    private Collection<NamingStrategyHelper> namingStrategyHelpers;

    private ApplicationContext applicationContext

    private boolean initialised

    StandardNamingStrategy() {
        initialised = false
        namingStrategyHelpers = []
    }

    StandardNamingStrategy(ApplicationContext applicationContext) {
        this()
        this.applicationContext = applicationContext
        initialise()
    }

    StandardNamingStrategy(Collection<NamingStrategyHelper> namingStrategyHelpers) {
        this()
        initialise namingStrategyHelpers
    }

    void initialise() {
        if (!applicationContext) applicationContext = Holders.getApplicationContext()
        initialise applicationContext.getBeansOfType(NamingStrategyHelper).values()
    }

    void initialise(NamingStrategyHelper namingStrategyHelper) {
        initialise([namingStrategyHelper])
    }

    void initialise(Collection<NamingStrategyHelper> namingStrategyHelpers) {
        this.namingStrategyHelpers = namingStrategyHelpers
        initialised = true
    }

    @Override
    String classToTableName(String className) {
        Collection<GrailsClass> artefacts = getDomainArtefacts(className)

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
        Collection<GrailsClass> artefacts = getDomainArtefacts(className)

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
        packageName.contains("datatype") ? "dt_$tableName" : tableName
    }

    String adjustTableName(Class clazz, String tableName, String packageName) {
        if (!initialised) initialise()
        NamingStrategyHelper helper = namingStrategyHelpers?.find {it.handlesClass(clazz)}
        if (helper) {
            return helper.adjustTableName(clazz, tableName, packageName)
        }

        def matcher = STANDARD_PACKAGE_PATTERN.matcher(packageName);
        if (!packageName.contains('datatype') && matcher.find()) {
            tableName = "${matcher.group('package')}_$tableName"
        }
        tableName
    }

    String classToSchemaName(Class clazz) {
        if (!initialised) initialise()
        canonicalNameToSchemaName(clazz.canonicalName)
    }

    String canonicalNameToSchemaName(String canonicalName) {
        if (!initialised) initialise()
        NamingStrategyHelper helper = namingStrategyHelpers?.find {it.handlesClass(canonicalName)}
        if (helper) {
            return helper.canonicalNameToSchemaName(canonicalName)
        }

        String schemaName = 'public'

        def matcher = STANDARD_DOMAIN_VERSION_PATTERN.matcher(canonicalName);
        if (matcher.find()) {
            schemaName = matcher.group('version')
        }
        schemaName
    }

    String propertyToColumnName(String propertyName) {
        if (!initialised) initialise()
        String columnName = super.propertyToColumnName(propertyName)
        NamingStrategyHelper helper = namingStrategyHelpers?.find {it.cleansColumnName(columnName)}
        helper ? helper.cleanColumnName(columnName) : columnName
    }

    List<String> getDataSources(String canonicalName) {
        if (!initialised) initialise()
        NamingStrategyHelper helper = namingStrategyHelpers?.find {it.handlesClass(canonicalName)}
        if (helper) {
            return helper.getDataSources(canonicalName)
        }
        ['DEFAULT']
    }

    static Collection<GrailsClass> getDomainArtefacts(String className) {
        Holders.grailsApplication.getArtefacts(DomainClassArtefactHandler.TYPE).findAll {it.fullName == className}
    }


}

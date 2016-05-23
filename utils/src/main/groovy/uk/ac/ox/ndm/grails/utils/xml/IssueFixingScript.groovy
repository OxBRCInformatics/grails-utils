package uk.ac.ox.ndm.grails.utils.xml

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @since 15/05/2016
 */
abstract class IssueFixingScript extends Script {

    Logger getLogger() {
        LoggerFactory.getLogger('uk.ac.ox.ndm.grails.utils.xml.' + getClass().simpleName)
    }

    void fixIssuesInXmlString(@ClosureParams(value = SimpleType, options = "java.lang.String") Closure<String> closure) {
        this.binding.fixedXml = closure.call(this.binding.xml)
    }
}

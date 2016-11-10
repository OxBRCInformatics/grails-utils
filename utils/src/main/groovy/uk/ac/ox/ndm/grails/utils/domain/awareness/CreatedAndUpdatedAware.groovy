package uk.ac.ox.ndm.grails.utils.domain.awareness

/**
 * @since 09/10/2015
 */
trait CreatedAndUpdatedAware {
    Date dateCreated
    Date lastUpdated

    Date getDateCreated() {
        dateCreated ?: new Date()
    }

    Date getLastUpdated() {
        lastUpdated ?: new Date()
    }
}
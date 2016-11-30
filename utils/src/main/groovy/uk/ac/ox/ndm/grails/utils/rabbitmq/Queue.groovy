package uk.ac.ox.ndm.grails.utils.rabbitmq

/**
 * @since 30/11/2016
 */
class Queue {

    String name
    String exchange
    Boolean durable
    Boolean autoDelete
    String binding


    Map asMap() {
        [
                name      : name,
                durable   : durable,
                autoDelete: autoDelete,
                exchange  : exchange,
                binding   : binding
        ]
    }
}

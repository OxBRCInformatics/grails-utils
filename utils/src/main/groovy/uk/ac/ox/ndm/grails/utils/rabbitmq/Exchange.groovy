package uk.ac.ox.ndm.grails.utils.rabbitmq

/**
 * @since 30/11/2016
 */
class Exchange {

    String name
    String type
    Boolean durable
    Boolean autoDelete

    List<ExchangeBinding> exchangeBindings

    Exchange addToBindings(ExchangeBinding binding){
        if(!exchangeBindings) exchangeBindings = []
        exchangeBindings += binding
        this
    }

    Exchange addToBindings(Map binding){
        addToBindings(new ExchangeBinding(binding))
    }

    Map asMap() {
        [
                name            : name,
                type            : type,
                durable         : durable,
                autoDelete      : autoDelete,
                exchangeBindings: exchangeBindings?.collect {it.asMap()} ?: []
        ]
    }
}

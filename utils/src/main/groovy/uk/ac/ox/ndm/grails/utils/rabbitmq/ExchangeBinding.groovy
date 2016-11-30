package uk.ac.ox.ndm.grails.utils.rabbitmq

/**
 * @since 30/11/2016
 */
class ExchangeBinding {

    String exchange
    String binding
    Origin origin

    Origin getOrigin(){
        origin?:Origin.DESTINATION
    }

    Map asMap() {
        [
                as      : getOrigin().name().toLowerCase(),
                exchange: exchange,
                binding : binding
        ]
    }
}


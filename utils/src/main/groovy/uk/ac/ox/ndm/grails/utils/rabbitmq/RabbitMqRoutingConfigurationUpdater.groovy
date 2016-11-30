package uk.ac.ox.ndm.grails.utils.rabbitmq

/**
 * @since 08/12/2016
 */
trait RabbitMqRoutingConfigurationUpdater implements BasicRabbitMqRoutingInfomationProvider{

    Map updateRabbitConfig(Map rabbitConfig) {

        if (!rabbitConfig || !rabbitConfig instanceof Map) throw new IllegalStateException('There must be a defined RabbitMq Map configuration')
        if (!(rabbitConfig.connection || rabbitConfig.connections))
            throw new IllegalStateException('There must be a defined RabbitMq connection or connections configuration')

        Set<Map> queuesConfig = rabbitConfig.queues ?: [] as HashSet
        Set<Map> exchangeConfig = rabbitConfig.exchanges ?: [] as HashSet

        getExchanges().each {exchange ->

            Map existing = exchangeConfig.find {it.name == exchange.name}
            if (existing) {
                if (exchange.exchangeBindings) {
                    existing.exchangeBindings = exchange.exchangeBindings.collect {it.asMap()}
                }
            }
            else {
                exchangeConfig.add exchange.asMap()
            }
        }

        getQueues().each { queue ->
            if(!queuesConfig.any{it.name == queue.name}) {
                queuesConfig.add queue.asMap()
            }
        }

        rabbitConfig.queues = queuesConfig
        rabbitConfig.exchanges = exchangeConfig
        rabbitConfig
    }
}

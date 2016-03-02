package uk.ac.ox.ndm.grails.utils.rabbitmq;

import java.util.Map;

/**
 * @since 02/03/2016
 */
public interface RabbitMqRoutingInfomationProvider {

    String getApplicationName();

    void setApplicationName(String pluginName);

    String getExchange();

    void setExchange(String exchange);

    Map getExchangeConfiguration();

    String getRoutingKey();

    void setRoutingKey(String routingKey);
}

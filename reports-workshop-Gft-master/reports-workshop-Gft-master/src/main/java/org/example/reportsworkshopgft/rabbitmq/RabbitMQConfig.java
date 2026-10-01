package org.example.reportsworkshopgft.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TIME_EXCHANGE = "ms-time.exchange";
    public static final String TRANSPORT_EXCHANGE = "trucks.exchange";
    public static final String PRODUCTION_EXCHANGE = "production.exchange";
    public static final String WAREHOUSE_EXCHANGE = "warehouses.exchange";

    public static final String TIME_ADVANCED_ROUTING_KEY = "time.advanced.v1";
    public static final String TIME_ADVANCED_QUEUE_NAME = "reports.time.advanced.v1";

    public static final String TRUCK_REGISTERED_ROUTING_KEY = "truck.registered.v1";
    public static final String TRUCK_REGISTERED_QUEUE_NAME = "reports.truck.registered.v1";

    public static final String TRUCK_POSITION_UPDATE_ROUTING_KEY = "truck.position.update.v1";
    public static final String TRUCK_POSITION_UPDATE_QUEUE_NAME =
            "reports.truck.position.update.v1";

    public static final String DELIVERY_COMPLETED_ROUTING_KEY = "delivery.completed.v1";
    public static final String DELIVERY_COMPLETED_QUEUE_NAME = "reports.delivery.completed.v1";

    public static final String TRUCK_STATUS_CHANGED_ROUTING_KEY = "truck.status.changed.v1";
    public static final String TRUCK_STATUS_CHANGED_QUEUE_NAME = "reports.truck.status.changed.v1";

    public static final String PRODUCTION_ORDER_CREATED_ROUTING_KEY = "production.order.created.v1";
    public static final String PRODUCTION_ORDER_CREATED_QUEUE_NAME =
            "reports.production.order.create.v1";

    public static final String PRODUCTION_ORDER_STARTED_ROUTING_KEY = "production.order.started.v1";
    public static final String PRODUCTION_ORDER_STARTED_QUEUE_NAME =
            "reports.production.order.started.v1";

    public static final String PRODUCTION_ORDER_BLOCKED_ROUTING_KEY = "production.order.blocked.v1";
    public static final String PRODUCTION_ORDER_BLOCKED_QUEUE_NAME =
            "reports.production.order.blocked.v1";

    public static final String PRODUCTION_ORDER_COMPLETED_ROUTING_KEY =
            "production.order.completed.v1";
    public static final String PRODUCTION_ORDER_COMPLETED_QUEUE_NAME =
            "reports.production.order.completed.v1";

    public static final String RECIPE_REGISTERED_ROUTING_KEY = "recipe.registered.v1";
    public static final String RECIPE_REGISTERED_QUEUE_NAME = "reports.recipe.registered.v1";

    public static final String FACTORY_REGISTERED_ROUTING_KEY = "factory.registered.v1";
    public static final String FACTORY_REGISTERED_QUEUE_NAME = "reports.factory.registered.v1";

    public static final String DELIVERY_CREATED = "delivery.created.v1";

    public static final String WAREHOUSE_STOCK_CHANGED_ROUTING_KEY = "warehouse.stock.changed.v1";
    public static final String WAREHOUSE_STOCK_CHANGED_QUEUE_NAME =
            "reports.warehouse.stock.changed.v1";

    public static final String REPLENISHMENT_REQUESTED_ROUTING_KEY = "replenishment.requested.v1";
    public static final String REPLENISHMENT_REQUESTED_QUEUE_NAME =
            "reports.replenishment.requested.v1";

    public static final String WAREHOUSE_ORDER_BLOCKED_ROUTING_KEY = "warehouse.order.blocked.v1";
    public static final String WAREHOUSE_ORDER_BLOCKED_QUEUE_NAME =
            "reports.warehouse.order.blocked.v1";

    public static final String WAREHOUSE_REGISTERED_ROUTING_KEY = "warehouse.registered.v1";
    public static final String WAREHOUSE_REGISTERED_QUEUE_NAME = "reports.warehouse.registered.v1";

    public static final String TRUCK_DELETED_ROUTING_KEY = "truck.deleted.v1";
    public static final String TRUCK_DELETED_QUEUE_NAME = "reports.truck.deleted.v1";

    @Bean
    public TopicExchange timeExchange() {
        TopicExchange exchange = new TopicExchange(TIME_EXCHANGE);
        exchange.setShouldDeclare(false);
        return exchange;
    }

    @Bean
    public TopicExchange transportExchange() {

        TopicExchange exchange = new TopicExchange(TRANSPORT_EXCHANGE);
        exchange.setShouldDeclare(false);
        return exchange;
    }

    @Bean
    public TopicExchange productionExchange() {

        TopicExchange exchange = new TopicExchange(PRODUCTION_EXCHANGE);
        exchange.setShouldDeclare(false);
        return exchange;
    }

    @Bean
    public TopicExchange warehouseExchange() {

        TopicExchange exchange = new TopicExchange(WAREHOUSE_EXCHANGE);
        exchange.setShouldDeclare(false);
        return exchange;
    }

    @Bean
    public Queue timeAdvancedQueue() {
        return new Queue(TIME_ADVANCED_QUEUE_NAME, true);
    }

    @Bean
    public Queue truckRegisteredQueue() {
        return new Queue(TRUCK_REGISTERED_QUEUE_NAME, true);
    }

    @Bean
    public Queue truckPositionUpdateQueue() {
        return new Queue(TRUCK_POSITION_UPDATE_QUEUE_NAME, true);
    }

    @Bean
    public Queue deliveryCreatedQueue() {
        return new Queue(DELIVERY_CREATED, true);
    }

    @Bean
    public Queue warehouseRegisteredQueue() {
        return new Queue(WAREHOUSE_REGISTERED_QUEUE_NAME, true);
    }

    @Bean
    public Queue deliveryCompletedQueue() {
        return new Queue(DELIVERY_COMPLETED_QUEUE_NAME, true);
    }

    @Bean
    public Queue productionOrderCreatedQueue() {
        return new Queue(PRODUCTION_ORDER_CREATED_QUEUE_NAME, true);
    }

    @Bean
    public Queue productionOrderStartedQueue() {
        return new Queue(PRODUCTION_ORDER_STARTED_QUEUE_NAME, true);
    }

    @Bean
    public Queue productionOrderBlockedQueue() {
        return new Queue(PRODUCTION_ORDER_BLOCKED_QUEUE_NAME, true);
    }

    @Bean
    public Queue productionOrderCompletedQueue() {
        return new Queue(PRODUCTION_ORDER_COMPLETED_QUEUE_NAME, true);
    }

    @Bean
    public Queue warehouseStockChangedQueue() {
        return new Queue(WAREHOUSE_STOCK_CHANGED_QUEUE_NAME, true);
    }

    @Bean
    public Queue replenishmentRequestedQueue() {
        return new Queue(REPLENISHMENT_REQUESTED_QUEUE_NAME, true);
    }

    @Bean
    public Queue truckStatusChangedQueue() {
        return new Queue(TRUCK_STATUS_CHANGED_QUEUE_NAME, true);
    }

    @Bean
    public Queue truckDeletedQueue() {
        return new Queue(TRUCK_DELETED_QUEUE_NAME, true);
    }

    @Bean
    public Queue recipeRegisteredQueue() {
        return new Queue(RECIPE_REGISTERED_QUEUE_NAME, true);
    }

    @Bean
    public Queue factoryRegisteredQueue() {
        return new Queue(FACTORY_REGISTERED_QUEUE_NAME, true);
    }

    @Bean
    public Binding timeAdvancedBinding() {
        return BindingBuilder.bind(timeAdvancedQueue())
                .to(timeExchange())
                .with(TIME_ADVANCED_ROUTING_KEY);
    }

    @Bean
    public Binding warehouseRegisteredBinding() {
        return BindingBuilder.bind(warehouseRegisteredQueue())
                .to(warehouseExchange())
                .with(WAREHOUSE_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding truckRegisteredBinding() {
        return BindingBuilder.bind(truckRegisteredQueue())
                .to(transportExchange())
                .with(TRUCK_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding truckDeletedBinding() {
        return BindingBuilder.bind(truckDeletedQueue())
                .to(transportExchange())
                .with(TRUCK_DELETED_ROUTING_KEY);
    }

    @Bean
    public Binding truckPositionUpdateBinding() {
        return BindingBuilder.bind(truckPositionUpdateQueue())
                .to(transportExchange())
                .with(TRUCK_POSITION_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryCreatedBinding() {
        return BindingBuilder.bind(deliveryCreatedQueue())
                .to(transportExchange())
                .with(DELIVERY_CREATED);
    }

    @Bean
    public Binding deliveryCompletedBinding() {
        return BindingBuilder.bind(deliveryCompletedQueue())
                .to(transportExchange())
                .with(DELIVERY_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding productionOrderCreatedBinding() {
        return BindingBuilder.bind(productionOrderCreatedQueue())
                .to(productionExchange())
                .with(PRODUCTION_ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding productionOrderStartedBinding() {
        return BindingBuilder.bind(productionOrderStartedQueue())
                .to(productionExchange())
                .with(PRODUCTION_ORDER_STARTED_ROUTING_KEY);
    }

    @Bean
    public Binding productionOrderBlockedBinding() {
        return BindingBuilder.bind(productionOrderBlockedQueue())
                .to(productionExchange())
                .with(PRODUCTION_ORDER_BLOCKED_ROUTING_KEY);
    }

    @Bean
    public Binding productionOrderCompletedBinding() {
        return BindingBuilder.bind(productionOrderCompletedQueue())
                .to(productionExchange())
                .with(PRODUCTION_ORDER_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding warehouseStockChangedBinding() {
        return BindingBuilder.bind(warehouseStockChangedQueue())
                .to(warehouseExchange())
                .with(WAREHOUSE_STOCK_CHANGED_ROUTING_KEY);
    }

    @Bean
    public Binding replenishmentRequestedBinding() {
        return BindingBuilder.bind(replenishmentRequestedQueue())
                .to(warehouseExchange())
                .with(REPLENISHMENT_REQUESTED_ROUTING_KEY);
    }

    @Bean
    public Binding truckStatusChangedBinding() {
        return BindingBuilder.bind(truckStatusChangedQueue())
                .to(transportExchange())
                .with(TRUCK_STATUS_CHANGED_ROUTING_KEY);
    }

    @Bean
    public Binding recipeRegisteredBinding() {
        return BindingBuilder.bind(recipeRegisteredQueue())
                .to(productionExchange())
                .with(RECIPE_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding factoryRegisteredBinding() {
        return BindingBuilder.bind(factoryRegisteredQueue())
                .to(productionExchange())
                .with(FACTORY_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue warehouseOrderBlockedQueue() {
        return new Queue(WAREHOUSE_ORDER_BLOCKED_QUEUE_NAME, true);
    }

    @Bean
    public Binding warehouseOrderBlockedBinding() {
        return BindingBuilder.bind(warehouseOrderBlockedQueue())
                .to(warehouseExchange())
                .with(WAREHOUSE_ORDER_BLOCKED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

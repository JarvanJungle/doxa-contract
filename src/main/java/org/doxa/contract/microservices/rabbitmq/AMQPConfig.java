package org.doxa.contract.microservices.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * @author vuducnoi
 * Rabbitmq configuration
 * Queue name pattern: <env>.queue.<unique_identify>.<service_receiver_id>.<resource_name>.<action>
 * ex: dev.queue.123.requisition.approval_matrix.updated
 * Exchange name pattern: <env>.exchange.<unique_identify><resource_name>
 * ex: dev.exchange.713.approval_matrix
 */
@Configuration
@EnableRabbit
public class AMQPConfig implements RabbitListenerConfigurer {

    /**
     * Exchange configuration for user updated event
     */
    @Bean
    public FanoutExchange userUpdatedExchange() {
        return new FanoutExchange(Queues.USER_DETAILS_UPDATED_EXCHANGE);
    }


    /**
     * @author vuducnoi
     * @description Send to requistion service whenever user details updated
     */
    @Bean
    public Queue userUpdatedRequistion() {
        return new Queue(Queues.USER_UPDATED_REQUISITION, true);
    }

    /**
     * @author vuducnoi
     * @description Send to order service whenever user details updated
     */
    @Bean
    public Queue userUpdatedOrders() {
        return new Queue(Queues.USER_UPDATED_ORDERS, true);
    }

    /**
     * @author vuducnoi
     * @description Sending emails
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(Queues.SENDING_EMAIL, true);
    }


    /**
     * Binding all queues to the exchange you want
     * ex:
     * Your exchange name is: dev.entity.exchange
     * and you want to deliver message to 2 queues: dev.entity.created, dev.entity.updated
     * you should bind 2 queues to the 'dev.entity.exchange' and publish message to the exchange
     * This pattern is called: fanout
     * @return
     */
//    @Bean
//    public List<Binding> binding() {
//        return Arrays.asList(
//                BindingBuilder.bind(sampleQueue()).to(exchange()),
//                BindingBuilder.bind(testQueue()).to(exchange()));
//    }

    @Bean
    public Binding bindUserUpdatedRequistion() {
        return BindingBuilder.bind(userUpdatedExchange()).to(userUpdatedExchange());
    }

    @Bean
    public Binding bindUserUpdatedOrder() {
        return BindingBuilder.bind(userUpdatedOrders()).to(userUpdatedExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /*
     * Inbound Configuration
     * We are using Annotation-Driven-Message-Listening, described here
     * http://docs.spring.io/spring-amqp/reference/htmlsingle/#async-annotation-driven
     */
    @Autowired
    public ConnectionFactory connectionFactory;

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    @Bean
    public MessageHandler eventResultHandler() {
        return new MessageHandler();
    }

    @Override
    public void configureRabbitListeners(
            RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }
}

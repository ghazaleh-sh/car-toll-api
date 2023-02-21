package ir.co.sadad.cartollapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitConfig {

//    public static final String QUEUE_NAME = "transaction-queue";
//    private static final String EXCHANGE_NAME = "ex-transaction-queue";
//    private static final String ROUTING_KEY = "transaction";
//
//    private static final String DEAD_LETTER_QUEUE_NAME = "transaction-dead-letter-queue";
//
//    @Value("${props.rabbit.host}")
//    private String host;
//    @Value("${props.rabbit.user}")
//    private String username;
//    @Value("${props.rabbit.password}")
//    private String password;
//
//    @Bean
//    public Queue queue() {
//        return QueueBuilder.durable(QUEUE_NAME)
//                .withArgument("x-dead-letter-exchange", "")
//                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_NAME).build();
//    }
//
//    @Bean
//    Queue dlq() {
//        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
//    }
//
//    @Bean
//    public Exchange exchange() {
//        return new DirectExchange(EXCHANGE_NAME);
//    }
//
//    @Bean
//    Binding binding(Queue queue, Exchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        connectionFactory.setPublisherReturns(true);
//        return connectionFactory;
//    }
//
//    @Bean
//    public AmqpAdmin amqpAdmin() {
//        return new RabbitAdmin(connectionFactory());
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }

}

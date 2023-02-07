package gutsandgun.kite_sendmanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;



    @Value("${rabbitmq.sms.queue1.name}")
    private String queue1;

    @Value("${rabbitmq.sms.queue2.name}")
    private String queue2;

    @Value("${rabbitmq.sms.queue3.name}")
    private String queue3;

    @Value("${rabbitmq.email.queue1.name}")
    private String emailQueue1;

    @Value("${rabbitmq.email.queue2.name}")
    private String emailQueue2;

    @Value("${rabbitmq.sms.queue1.exchange}")
    private String exchange1;

    @Value("${rabbitmq.sms.queue2.exchange}")
    private String exchange2;

    @Value("${rabbitmq.sms.queue3.exchange}")
    private String exchange3;

    @Value("${rabbitmq.email.queue1.exchange}")
    private String emailExchange;

    @Value("${rabbitmq.sms.routing.key.queue1}")
    private String routingKey1;

    @Value("${rabbitmq.sms.routing.key.queue2}")
    private String routingKey2;

    @Value("${rabbitmq.sms.routing.key.queue3}")
    private String routingKey3;


    @Value("${rabbitmq.email.routing.key.queue1}")
    private String emailRoutingKey1;

    @Value("${rabbitmq.email.routing.key.queue2}")
    private String emailRoutingKey2;


    @Bean
    Queue queue1() {
        return new Queue(queue1, true);
    }
    @Bean
    Queue queue2() {
        return new Queue(queue2, true);
    }
    @Bean
    Queue queue3() {
        return new Queue(queue3, true);
    }

    @Bean
    Queue emailQueue1() {
        return new Queue(emailQueue1, true);
    }

    @Bean
    Queue emailQueue2() {
        return new Queue(emailQueue2, true);
    }

    @Bean
    DirectExchange directExchange1() {
        return new DirectExchange(exchange1);
    }

    @Bean
    DirectExchange directExchange2() {
        return new DirectExchange(exchange2);
    }

    @Bean
    DirectExchange directExchange3() {
        return new DirectExchange(exchange3);
    }

    @Bean
    DirectExchange emailDirectExchange1() {
        return new DirectExchange(emailExchange);
    }

    @Bean
    Binding binding1() {
        return BindingBuilder.bind(queue1()).to(directExchange1()).with(routingKey1);
    }

    @Bean
    Binding binding2() {
        return BindingBuilder.bind(queue2()).to(directExchange2()).with(routingKey2);
    }

    @Bean
    Binding binding3() {
        return BindingBuilder.bind(queue3()).to(directExchange3()).with(routingKey3);
    }

    @Bean
    Binding emailBinding1() {
        return BindingBuilder.bind(emailQueue1()).to(emailDirectExchange1()).with(emailRoutingKey1);
    }

    @Bean
    Binding emailBinding2() {
        return BindingBuilder.bind(emailQueue2()).to(emailDirectExchange1()).with(emailRoutingKey2);
    }


    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }



}
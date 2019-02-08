package com.mavs.relationshipservice.config;

import com.mavs.activity.model.ActivityType;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    private static final String QUEUE = "_QUEUE";

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue newRelationship() {
        return new Queue(ActivityType.NEW_RELATIONSHIP.name() + QUEUE, true, false, false);
    }

    @Bean
    public Queue endRelationship() {
        return new Queue(ActivityType.END_RELATIONSHIP.name() + QUEUE, true, false, false);
    }
}

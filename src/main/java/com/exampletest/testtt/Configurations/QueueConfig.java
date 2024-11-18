package com.exampletest.testtt.Configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    @Bean
    public Queue queueFreeOrders() {
        return new Queue("queueFreeOrders");
    }
    @Bean
    public Queue queueErrors() {
        return new Queue("queueErrors");
    }
}

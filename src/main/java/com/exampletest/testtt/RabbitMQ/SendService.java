package com.exampletest.testtt.RabbitMQ;

import com.exampletest.testtt.models.tms.search.orders.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("queueFreeOrders")
    private Queue queueFreeOrders;

    @Autowired
    @Qualifier("queueErrors")
    private Queue queueErrors;

    public void sendFreeOrders(String json) {
        rabbitTemplate.convertAndSend(queueFreeOrders.getName(), json);
    }

    public void sendErrors(String msg) {
        rabbitTemplate.convertAndSend(queueErrors.getName(), msg);
        log.info("Rabbit queueErrors:{}", msg);
    }
}

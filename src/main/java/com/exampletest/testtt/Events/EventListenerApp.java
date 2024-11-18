package com.exampletest.testtt.Events;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Schedulers.SchedulerNew;
import com.exampletest.testtt.Schedulers.SchedulerSearchOrders;
import com.exampletest.testtt.Service.CardTransportService;
import com.exampletest.testtt.Service.TokenService;
import com.sun.jdi.event.ExceptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListenerApp {
    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;
    @Autowired
    private SchedulerSearchOrders schedulerSearchOrders;

    @Autowired
    TokenService tokenService;
    @Autowired
    CardTransportService cardTransportService;

    @Autowired
    DataMemory dataMemory;

    @Autowired
    private SchedulerNew schedulerNew;
    @EventListener(ExceptionEvent.class)
    public void handleStartEvents() {
        log.info("Application started");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleMultipleEvents() {
        postProcessor.postProcessBeforeDestruction(schedulerSearchOrders, "");
        postProcessor.postProcessBeforeDestruction(schedulerNew, "");
    }
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        dataMemory.setTokens(tokenService.getTokens().collectList().block());
        dataMemory.setCardtransports(cardTransportService.findAll().collectList().block());
    }

}

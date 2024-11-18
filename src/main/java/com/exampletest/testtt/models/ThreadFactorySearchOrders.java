package com.exampletest.testtt.models;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.RabbitMQ.SendService;
import com.exampletest.testtt.models.tms.Entity.Token;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

@Data
@Slf4j
public class ThreadFactorySearchOrders implements ThreadFactory {
    String threadName;
    String token;
    DataMemory dataMemory;
    WebClient webClient;
    SendService sendService;
    @Override
    public Thread newThread(@NotNull Runnable r) {
        return new ThreadSearchOrders(threadName, token, dataMemory, sendService, webClient);
    }
}

package com.exampletest.testtt.Schedulers;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.Service.WebFluxWebSocketHandler;
import com.exampletest.testtt.WebJobs.HttpsHelpers;
import com.exampletest.testtt.models.tms.Entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class SchedulerCheckToken {
    @Autowired
    private TokenService tokenService;

    @Autowired
    DataMemory dataMemory;

    @Autowired
    WebFluxWebSocketHandler webFluxWebSocketHandler;

    @Scheduled(fixedRate = 30000)
    public void checkToken() {
        List<Token> tokens = tokenService.getTokens().collectList().block();
        HttpsHelpers httpsHelpers = new HttpsHelpers();
        List<String> ts = new ArrayList<>();
        if (tokens != null && !tokens.isEmpty()) {
            for (Token token : tokens) {
                String token_str = httpsHelpers.tokenChecking(token.getToken());
                if (!token_str.equals("OK: Токен впорядке")) {
                    ts.add(token.getToken());
                    tokenService.delete(token.getToken()).subscribeOn(Schedulers.boundedElastic()).subscribe();
                }
            }
            dataMemory.setTokens(tokens.stream().filter(f -> !ts.contains(f.getToken())).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    @Scheduled(fixedRate = 5000)
    public void checkToken2() {
        System.out.println(Thread.currentThread().getName());
        WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();
        webSocketClient.execute(
                URI.create("ws://localhost:8080/ws"),
                session -> session.send(Mono.just(session.textMessage("Hello " + Thread.currentThread().getName())))
        ).block();
    }
}

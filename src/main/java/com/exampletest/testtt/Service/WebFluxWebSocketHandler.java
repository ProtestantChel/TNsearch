package com.exampletest.testtt.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Event;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class WebFluxWebSocketHandler implements WebSocketHandler {

    @Override
    public @NotNull Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> stringFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(String::toUpperCase)
                .map(session::textMessage).log();
        return session.send(stringFlux);

    }
}

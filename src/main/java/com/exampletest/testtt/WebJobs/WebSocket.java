package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.RabbitMQ.SendService;
import com.exampletest.testtt.Service.WebFluxWebSocketHandler;
import com.exampletest.testtt.models.WebSocketRequest;
import com.google.gson.Gson;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class WebSocket {
    @Autowired
    SendService sendService;
    @Autowired
    WebFluxWebSocketHandler webFluxWebSocketHandler;
    private final WebSocketClient client = new ReactorNettyWebSocketClient(Helpers.httpClient());

    public Mono<Void> receive(URI uri) {
        System.out.println(Thread.currentThread().getName());
        return client.execute(uri, session ->
                session.receive()
                        .doOnNext(next -> {
                            String body = next.getPayloadAsText(StandardCharsets.UTF_8);
                            if (body.contains("created") && (body.contains("order")|| body.contains("auction"))){
                                WebSocketRequest webSocketRequest = new Gson().fromJson(body, WebSocketRequest.class);
                                sendService.sendErrors("WEB_SOCKET:&" + webSocketRequest.getPayload().getId());
                            }

                        })
                        .doOnComplete(() -> System.out.println("End"))
                        .then());
    }
    public Mono<Void> local(Object object) {
        String payload = new Gson().toJson(object);
        URI uri = URI.create("ws://localhost:8080/ws");
        return client.execute(uri, session ->
                session.send(
                        Mono.just(session.textMessage(payload))
                )).then();
    }
}

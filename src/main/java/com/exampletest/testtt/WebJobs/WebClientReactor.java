package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.Configurations.VariablesProperties;
import com.exampletest.testtt.models.TmsErrors;
import com.exampletest.testtt.models.tms.Entity.Token;
import com.exampletest.testtt.models.tms.search.orders.FiasRequest;
import com.exampletest.testtt.models.tms.search.orders.OrdersJSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebClientReactor {

    @Autowired
    VariablesProperties variablesProperties;




    WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(Helpers.httpClient()))
            .build();

    public Mono<OrdersJSON> findFreeOrder(String uri, String token) {
       return webClient.post()
               .uri(uri)
               .contentType(MediaType.APPLICATION_JSON)
               .headers(Helpers.getConsumerHeaders(token))
               .bodyValue(Helpers.freeOrders)
               .retrieve()
//               .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//                   logTraceResponse(clientResponse);
//                   return clientResponse.createException().flatMap(f -> Mono.error(new RuntimeException("Токен устарел, авторизация невозможна.")));
//               })
               .bodyToMono(OrdersJSON.class)
               .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
               .onErrorResume(throwable -> Mono.just(new OrdersJSON()));

    }

    public Mono<FiasRequest> findFias(String uri, String token) {
        System.out.println(uri);
        return webClient.get()
                .uri(uri)
                .headers(Helpers.getConsumerHeaders(token))
                .retrieve()
                .bodyToMono(FiasRequest.class);
    }

    public void logTraceResponse(ClientResponse response) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare("logs", "fanout");
            channel.basicPublish("logs", "", null, "body.getMessage()".getBytes(StandardCharsets.UTF_8));

            response.bodyToMono(TmsErrors.class)
                    .publishOn(Schedulers.boundedElastic())
                    .subscribe(body -> {
                        log.error("body: {}",body.getMessage());
                        try {

                        }catch (Exception e) {log.error(e.getMessage(),e);}

                    });

        } catch (Exception e) {log.error(e.getMessage(),e);}
    }

}

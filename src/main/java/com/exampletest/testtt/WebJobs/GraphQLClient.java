package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.models.tms.search.orders.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

public class GraphQLClient {

    private static final Logger log = LoggerFactory.getLogger(GraphQLClient.class);
    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(Helpers.httpClient()))
            .build();

    public List<Orders> freeOrders(String token, List<String> list) throws WebClientResponseException {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
                .url("https://api.transport2.ru/carrier/graphql")
                .headers(Helpers.getConsumerHeaders(token))
                .build();
        return graphQlClient.document(Helpers.freeOrdersGraphQL(list))
                .retrieveSync("freeOrders")
                .toEntity(List.class);
    }
    public Mono<List>  auctionNewOrders(String token, List<String> list) throws WebClientResponseException {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
                .url("https://api.transport2.ru/carrier/graphql")
                .headers(Helpers.getConsumerHeaders(token))
                .build();
        return graphQlClient.document(Helpers.auctionNewOrdersGraphQL(list))
                .retrieve("auctionOrders")
                .toEntity(List.class);
    }

    public void bookOrders(String token, String id) throws WebClientResponseException, FieldAccessException {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
                .url("https://api.transport2.ru/carrier/graphql")
                .headers(Helpers.getConsumerHeaders(token))
                .build();
        List<Orders> orders = graphQlClient.document(Helpers.bookOrderGraphQL(id))
                .retrieveSync("bookOrder")
                .toEntity(List.class);
        log.info("bookOrders {} {}", id, orders);
    }
}

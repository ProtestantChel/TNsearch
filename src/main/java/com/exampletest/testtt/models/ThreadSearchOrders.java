package com.exampletest.testtt.models;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.RabbitMQ.SendService;
import com.exampletest.testtt.WebJobs.GraphQLClient;
import com.exampletest.testtt.WebJobs.Helpers;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.search.orders.Orders;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.netty.handler.proxy.ProxyConnectException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.transport.ProxyProvider;

import javax.net.ssl.SSLException;
import java.util.List;

@Slf4j
public class ThreadSearchOrders extends Thread {
    String threadName;
    Thread thread;
    String token;
    DataMemory dataMemory;
    SendService sendService;
    WebClient webClient;

    public ThreadSearchOrders(String threadName, String token, DataMemory dataMemory, SendService sendService, WebClient webClient) {
        this.threadName = threadName;
        this.thread = new Thread(this, threadName);
        this.token = token;
        this.dataMemory = dataMemory;
        this.sendService = sendService;
        this.webClient = webClient;
    }

    public void run() {
        try {
            HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
                    .url("https://api.transport2.ru/carrier/graphql")
                    .headers(Helpers.getConsumerHeaders(token))
                    .build();
            List<Orders> s = graphQlClient.document(Helpers.freeOrdersGraphQL(dataMemory.getCardtransports().stream().map(Cardtransport::getPlaceofloadingfias).toList()))
                    .retrieveSync("freeOrders")
                    .toEntity(List.class);

            if (s != null && !s.isEmpty()) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(s);
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    sendService.sendFreeOrders(jsonArray.getJSONObject(i).toString());
                }
            }
//            System.out.println("Finish: " + endTime + ": " + Thread.currentThread().getName() + " " + s);
        } catch (WebClientResponseException ignored) {
        } catch (GraphQlTransportException | JacksonException e) {
            log.error(e.getMessage(), e);
        }

    }

    private WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        Helpers.httpClient()))
                .build();
    }
    private WebClient webClientProxy(ProxyServerItem proxyServerItem) throws ProxyConnectException, SSLException {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        Helpers.httpClient().proxy(typeSpec -> typeSpec
                                .type(ProxyProvider.Proxy.HTTP)
                                .host(proxyServerItem.getHost())
                                .port(proxyServerItem.getPort())
                                .username(proxyServerItem.getUsername())
                                .password(pass -> proxyServerItem.getPassword())
                        )))
                .build();
    }
}

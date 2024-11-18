package com.exampletest.testtt.Schedulers;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Configurations.VariablesProperties;
import com.exampletest.testtt.RabbitMQ.SendService;
import com.exampletest.testtt.Service.CardTransportService;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.WebJobs.GraphQLClient;
import com.exampletest.testtt.WebJobs.Helpers;
import com.exampletest.testtt.WebJobs.HttpsHelpers;
import com.exampletest.testtt.models.ProxyServerItem;
import com.exampletest.testtt.models.ThreadFactorySearchOrders;
import com.exampletest.testtt.models.ThreadSearchOrders;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.Entity.Token;
import com.exampletest.testtt.WebJobs.WebClientReactor;
import com.exampletest.testtt.models.tms.search.orders.Orders;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import io.netty.handler.proxy.ProxyConnectException;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.transport.ProxyProvider;

import javax.net.ssl.SSLException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
//@EnableAsync
@Slf4j
@Data
public class SchedulerSearchOrders {
    @Autowired
    VariablesProperties variablesProperties;

    @Autowired
    WebClientReactor webClientReactor;

    @Autowired
    TokenService tokenService;
    @Autowired
    CardTransportService cardTransportService;
    @Autowired
    SendService sendService;

    @Autowired
    DataMemory dataMemory;

    private ExecutorService service_null = Executors.newWorkStealingPool(2);
    private List<ExecutorService> service_proxies = new ArrayList<>();
    private List<WebClient> webClients = new ArrayList<>();

    @PostConstruct
    private void schedulerSearchOrders (){
        webClients.add(webClient());
        if (variablesProperties.getProxies().contains(";")){
            String[] proxiesArray = variablesProperties.getProxies().split(";");
            for (String proxy : proxiesArray) {
                if (proxy.contains(":")) {
                    String[] proxyArray = proxy.split(":");
                    if (proxyArray.length == 4) {
                        service_proxies.add(Executors.newWorkStealingPool(2));
                        webClients.add(webClientProxy(new ProxyServerItem(proxyArray[0], Integer.parseInt(proxyArray[1]), proxyArray[2], proxyArray[3])));
                    }
                }
            }
        }

    }

    @Scheduled(fixedRateString = "${orders.scheduled.timeout}", initialDelay = 500)
    public void searchOrders() {
        for (int i = 0; i <= dataMemory.getTokens().size(); i++) {
            if (i < 1) {
                service_null
                        .submit(new ThreadSearchOrders("search-freeOrders-" + (i + 1), dataMemory.getTokens().get(i).getToken(), dataMemory,  sendService, webClients.get(0)));
            } else {
                try {
                    if (webClients.size() > i) {
                        service_proxies.get(i - 1)
                                .submit(new ThreadSearchOrders("search-freeOrders-" + (i + 1), dataMemory.getTokens().get(i).getToken(), dataMemory, sendService, webClients.get(i)));
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
            sleep(50);
        }
    }
    private List<Orders> stringToListOrders(String message) {
        String json = message.split("&")[1];
        Type listType = new TypeToken<List<Orders>>() {}.getType();
        List<Orders> orders_tmp = new Gson().fromJson(json, listType);
        return orders_tmp.stream().peek(m -> {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            try {
                Date now = formatter.parse(m.getLoadingDatetime());
                m.setLoadingDatetime(String.valueOf(now.getTime()));
            } catch (ParseException e) {
                log.info(e.getMessage(), e);
                m.setLoadingDatetime("");
            }
        }).toList();
    }
    private Long dateLong (Long l){
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(l));
        stringBuilder.replace(stringBuilder.length() - 3, stringBuilder.length(), "000");
        return Long.parseLong(stringBuilder.toString());
    }
    private List<Cardtransport> stringToListCardtransport(String message) {
        String json = message.split("&")[2];
        Type listType = new TypeToken<List<Cardtransport>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        Helpers.httpClient()))
                .build();
    }
    private WebClient webClientProxy(ProxyServerItem proxyServerItem){
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

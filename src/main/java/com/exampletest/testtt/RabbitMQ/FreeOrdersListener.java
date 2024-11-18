package com.exampletest.testtt.RabbitMQ;


import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Configurations.VariablesProperties;
import com.exampletest.testtt.WebJobs.EmailSend;
import com.exampletest.testtt.WebJobs.GraphQLClient;
import com.exampletest.testtt.WebJobs.HttpsHelpers;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;

import com.exampletest.testtt.models.tms.Entity.Token;
import com.exampletest.testtt.models.tms.search.orders.Orders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Slf4j
@Component
@RabbitListener(queues = "queueFreeOrders")
public class FreeOrdersListener {


    @Autowired
    VariablesProperties variablesProperties;
    @Autowired
    DataMemory dataMemory;



    @RabbitHandler
    public void receive(String json) {
        Orders orders = new Gson().fromJson(json, Orders.class);
//        System.out.println(orders);
        if (orders != null) {
            List<Cardtransport> cardtransports = dataMemory.getCardtransports();

//            System.out.println("TH_ПОИСК_NEW Найдена заявка №" + orders.getExternalNo());
            for (Cardtransport cardtransport : cardtransports) {
                boolean onNum =cardtransport.getOnnum().contains(",") ? Arrays.stream(cardtransport.getOnnum().split(",")).anyMatch(a -> a.trim().equalsIgnoreCase(orders.getExternalNo())) : cardtransport.getOnnum().equalsIgnoreCase(orders.getExternalNo());
                if (cardtransport.getPlaceofloading().contains(orders.getLoadingPlaces().get(0).getStoragePoint().getSettlement())) {
                    if (dateLong(dateFormant(orders.getLoadingDatetime())) >= dateLong(cardtransport.getShipmentstart()) && Arrays.stream(cardtransport.getOnnum().split(",")).map(String::trim).noneMatch(a -> a.equalsIgnoreCase(orders.getExternalNo()))) {
                        try {
                            System.out.println("Взять заявку №" + orders.getExternalNo() + " #" + orders.getId());
                            System.out.println(orders.toString());
                            if (!dataMemory.getTokens().isEmpty())
                                new GraphQLClient().bookOrders(dataMemory.getTokens().get(0).getToken(), orders.getId());
                        } catch (FieldAccessException e) {
                            if (e.getMessage().contains("Операция с заявкой может быть выполнена только в следующих статусах: [FREE]")) {
                                log.error(e.getMessage());
                                sleep(1000);
                            }
                        }
                    }
                }

            }
//        if (message != null && message.contains("WEB_SOCKET")) {
//            String id = message.split("&")[1];
//            log.info("TH_ПОИСК_NEW WEBSOCKET Найдена заявка №" + id);
////                    EmailSend emailSend = new EmailSend();
////                    emailSend.mailSend("Найдена заявка №" + id, log, variablesProperties, "TH_ПОИСК_NEW WEB_SOCKETНайдена заявка №" + id);
//        }
        }
    }

    private Long dateLong (Long l){
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(l));
        stringBuilder.replace(stringBuilder.length() - 3, stringBuilder.length(), "000");
        return Long.parseLong(stringBuilder.toString());
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
    private long dateFormant(String date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date now = formatter.parse(date);
            return now.getTime();
        } catch (ParseException e) {
            log.info(e.getMessage(), e);
            return 0;
        }
    }

//    private List<Cardtransport> stringToListCardtransport(String message) {
//        String json = message.split("&")[2];
//        Type listType = new TypeToken<List<Cardtransport>>() {}.getType();
//        return new Gson().fromJson(json, listType);
//    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}

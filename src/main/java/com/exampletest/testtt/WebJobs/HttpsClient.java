package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.RabbitMQ.SendService;
import com.exampletest.testtt.models.tms.search.orders.Orders;
import com.exampletest.testtt.models.tms.search.orders.OrdersJSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HttpsClient {
    @Autowired
    SendService sendService;


    private final Set<String> set = new HashSet<>();

//    public void sendFreeOrders() {
//
//        try {
//            Connection.Response response = Jsoup.connect("https://api.transport2.ru/carrier/graphql?operation=freeOrders")
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
//                    .headers(Helpers.getHeaders("7047b6dae4f6e4f5f0eb66395299d88ab9bcb67f"))
//                    .ignoreContentType(true)
//                    .ignoreHttpErrors(true)
//                    .data(Helpers.freeOrders)
//                    .method(Connection.Method.POST)
//                    .execute();
//            try {
//                OrdersJSON ordersJSON = new Gson().fromJson(response.body(), OrdersJSON.class);
//                List<String> id_list = new ArrayList<>(ordersJSON.getData().getFreeOrders().stream().map(Orders::getId).toList());
//                if (id_list.size() > set.size()) {
//                    List<String> list = id_list.stream().filter(f -> !set.contains(f)).toList();
//                    for (String msg: list){
//                        id_list.removeIf(element -> element.equals(msg));
//                        sendService.sendFreeOrders(msg);
//                    }
//
//                }
//                set.addAll(id_list);
//            } catch (Exception exception){
//                log.error("{} body: {} status: {}", exception.getMessage(), response.body(), response.statusMessage(), exception);
//            }
//
//        } catch (Exception exception){
//            log.error(exception.getMessage(), exception);
//        }
//    }


}

package com.exampletest.testtt.WebJobs;


import com.exampletest.testtt.Configurations.VariablesProperties;
import com.exampletest.testtt.RabbitMQ.SendService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HttpsHelpers {

    public Connection.Response httpsRequest(String url, Map<String,String> headers, Map<String,String> body, Connection.Method method) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .headers(headers)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .data(body)
                .method(method)
                .execute();
    }
    public Connection.Response httpsRequest(String url, Map<String,String> headers, String body, Connection.Method method) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .headers(headers)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .requestBody(body)
                .method(method)
                .execute();
    }
    public String getToken(VariablesProperties mcfg){
        try {
            Connection.Response response_email = httpsRequest(
                    "https://id.tn.ru/api/v1/auth/send/email",
                    Map.of("Accept","application/json, text/plain, */*"),
                    "{\"email\":\"" + mcfg.getUser() +"\",\"locale\":\"ru_RU\",\"repeat\":0,\"code_length\":6,\"fingerprint\":\"ab002224da1e768314907e1966ecaded\"}",
                    Connection.Method.POST
            );
            log.info(response_email.body());
            if (response_email.statusCode() != 200) {
                log.error(response_email.statusMessage() + " " + response_email.body());
                return response_email.statusMessage() + " " + response_email.body();
            }
            ResponseJsonToken jsonToken  = new Gson().fromJson(response_email.body(), ResponseJsonToken.class);
            log.info(jsonToken.getToken());
            EmailSend emailSend = new EmailSend();
            String code = emailSend.getEmailCode(mcfg);
            log.info(code);

            Connection.Response response_accept_code = httpsRequest(
                    "https://id.tn.ru/api/v1/auth/accept_code",
                    Map.of("Content-Type", "application/x-www-form-urlencoded", "Accept","application/json, text/plain, */*"),
                    "{\"code\":\"" + code + "\", \"token\":\"" + jsonToken.getToken() + "\"}",
                    Connection.Method.POST);

            if (response_accept_code.statusCode() != 200) {
                log.error(response_accept_code.statusMessage() + " " + response_accept_code.body());
                return response_accept_code.statusMessage() + " " + response_accept_code.body();
            }

            jsonToken  = new Gson().fromJson(response_accept_code.body(), ResponseJsonToken.class);

            Connection connection_session = Jsoup.connect("https://id.tn.ru/api/v1/auth/session");
            Document doc_session = connection_session
                    .ignoreContentType(true)
                    .header("Content-Type", "application/json")
                    .header("Accept","application/json, text/plain, */*")
                    .requestBody("{\"token\":\"" + jsonToken.getToken() + "\"}")
                    .post();
            Map<String, String> session_id = connection_session.response().cookies();


            Connection connection_continue = Jsoup.connect("https://id.tn.ru/api/v1/auth/continue?client_id=0e2e90c2-c6a8-4c9a-8af1-0ba0397deb5b&redirect_uri=https%3A%2F%2Fapi.transport2.ru%2Fdigital-profile%2Fcallback&response_type=code").followRedirects(true);
            connection_continue.cookies(session_id);
            connection_continue.get();

            String url_2 = connection_continue.response().url().toString();

            String token = Arrays.stream(url_2.split("&")).filter(f -> f.contains("token")).map(m -> m.split("=")[1]).collect(Collectors.joining());
            log.info(token);
//            new H2DataSQL().updateToken(token);
            return token;
        } catch (Exception e){

            log.error(e.getMessage(),e);
            return "Ошибка входа на transport2.ru";
        }
    }

    public String tokenChecking(String token) {
        try {
            Connection.Response response = new HttpsHelpers().httpsRequest(
                    "https://api.transport2.ru/carrier/graphql?operation=ordersCounters",
                    Helpers.getHeaders(token),
                    Map.of(
                            "operationName","ordersCounters",
                            "query", "query ordersCounters {\n  ordersCounters {\n    free\n    assigned\n    transportReserved\n    readyToGo\n    __typename\n  }\n}\n",
                            "variables", "{}"
                    ),
                    Connection.Method.POST);
            if (response.statusCode() == 200)
                return "OK: Токен впорядке";
            if (response.statusCode() == 401){
                if (response.body().contains("Токен устарел, авторизация невозможна.")){
                    log.error("Токен устарел, авторизация невозможна.");
                    return "Предупреждение: Токен устарел, авторизация невозможна.";
                }
            }
            log.error("Ошибка: Не удалось получить Токен. status code: {}, body: {}",response.statusCode(),response.body());
            return String.format("Ошибка: Не удалось получить Токен. status code: %d, body: %s",response.statusCode(),response.body());
        } catch (Exception e) {
            log.error("Фатальная ошибка: Токен получить невозможно. Смотри лог программы! {}", e.getMessage(), e);
            return String.format("Фатальная ошибка: Токен получить невозможно. Смотри лог программы! %s", e.getMessage());

        }

    }
}

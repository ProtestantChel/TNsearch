package com.exampletest.testtt.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebConfiguration {
    @Bean
    RouterFunction<ServerResponse> spaRouter() {
        ClassPathResource index = new ClassPathResource("static/index.html");
        return route().resource(path("/"), index).build();
    }
}

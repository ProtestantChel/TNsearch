package com.exampletest.testtt;

import com.exampletest.testtt.WebJobs.WebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class TestttApplication {
	private static ConfigurableApplicationContext context;
	public static void main(String[] args) throws URISyntaxException {

        context = SpringApplication.run(TestttApplication.class, args);
//		WebSocket webSocket = context.getBean("webSocket", WebSocket.class);
//		Mono<Void> receive = webSocket.receive(new URI("wss://api.transport2.ru/ws/notifications/?token=7804b303575a73b98cbb41e64dbe50e631dbc677"));
//		ExecutorService rabbitmq_svr_thread = Executors.newSingleThreadExecutor();
//		rabbitmq_svr_thread.submit(() -> {
//			receive.block();
//		});


	}

}

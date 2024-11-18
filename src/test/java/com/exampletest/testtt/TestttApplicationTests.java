package com.exampletest.testtt;

import com.exampletest.testtt.WebJobs.*;


import com.exampletest.testtt.models.tms.search.orders.Orders;
import com.exampletest.testtt.models.tms.search.orders.OrdersJSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;

import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.transport.ProxyProvider;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@SpringBootTest
class TestttApplicationTests {

//	@Autowired
//	private RabbitTemplate rabbitTemplate;
//	@Test
//	void contextLoads() throws URISyntaxException, IOException, TimeoutException {
//
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		factory.setPort(5672);
//		Channel channel = factory.newConnection().createChannel();
//		channel.queueDeclare("FREE_ORDERS", false, false, false, null);
//
//		WebSocketClient client = new ReactorNettyWebSocketClient(httpClient());
//
//		URI url = new URI("wss://api.transport2.ru/ws/notifications/?token=e3b93246b57911ca5c2bced4085f1cd5d453664f");
//		for (int i = 0; i <= 10; i++) {
//			try {
//
//				Mono<Void> mono = client.execute(url, session ->
//						session.receive()
//								.doOnNext(next -> {
//									String body = next.getPayloadAsText(StandardCharsets.UTF_8);
//									if (body.contains("created")){
//										log.info(Thread.currentThread().getName() + " Received the news:" + body);
//									}
//								})
//								.doOnComplete(() -> System.out.println("End"))
//								.then());
//				ExecutorService service = Executors.newSingleThreadExecutor();
//				service.submit(() -> mono.block());
//				System.out.println("i = " + i);
//				break;
//			} catch (WebSocketClientHandshakeException e) {
//				log.error("WebSocket Client Handshake Exception", e);
//				url = new URI("wss://api.transport2.ru/ws/notifications/?token=e3b93246b57911ca5c2bced4085f1cd5d453664f");
//			}
//		}
//
//		ExecutorService service = Executors.newSingleThreadExecutor();
//		service.submit(() -> {
//			Map<String,String> map_1 = new HashMap<>();
//			map_1.put("Accept", "application/json, text/plain, */*");
//			map_1.put("Referer", "https://tms.transport2.ru/");
//			map_1.put("Authorization", "Token e3b93246b57911ca5c2bced4085f1cd5d453664f");
//
//			Map<String,String> map_2 = new HashMap<>();
//			map_2.put("operationName", "freeOrders");
//			map_2.put("variables", "{\n" +
//					"    \"totalCount\": true,\n" +
//					"    \"loadingPlaceByFiasCodes\": [],\n" +
//					"    \"unloadingPlaceByFiasCodes\": [],\n" +
//					"    \"status\": \"FREE\",\n" +
//					"    \"first\": 1000,\n" +
//					"    \"offset\": 0,\n" +
//					"    \"sortBy\": [\n" +
//					"      \"is_shipment_on_agreed_date_and_status_not_completed\",\n" +
//					"      \"-id\"\n" +
//					"    ]\n" +
//					"  }");
//			map_2.put("query", "query freeOrders($externalNo: ID, $id: ID) {\n  freeOrders(\n    externalNo: $externalNo\n    id: $id\n  ) {\n    id\n   externalNo\n}\n}\n");
//			List<String> list = new ArrayList<>();
//			while (true) {
//				try {
//					Connection.Response response = Jsoup.connect("https://api.transport2.ru/carrier/graphql?operation=freeOrders")
//							.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
//							.headers(map_1)
//							.ignoreContentType(true)
//							.ignoreHttpErrors(true)
//							.data(map_2)
//							.execute();
//					OrdersJSON ordersJSON = new Gson().fromJson(response.body(), OrdersJSON.class);
//					List<String> id_list = ordersJSON.getData().getFreeOrders().stream().map(Orders::getId).toList();
//					List<String> finalList = list;
//					List<String> free_list_new = id_list.stream().filter(f -> !finalList.contains(f)).toList();
//					if (!free_list_new.isEmpty()){
//						log.info("FREE ORDERS: " + ordersJSON.getData().getFreeOrders());
//					}
//					list = new ArrayList<>(id_list);
//
//
//					TimeUnit.MILLISECONDS.sleep(100);
//				} catch (Exception exception){
//					log.error(exception.getMessage(), exception);
//					break;
//				}
//			}
//		});
//
//
//
//
//
//		new Scanner(System.in).nextLine(); // Don't close immediately.
//	}
//	public HttpClient httpClient() {
//
//		TcpClient tcpClient = TcpClient.create()
//				.secure(t -> {
//					t.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE));
//				})
//				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
//				.doOnConnected(connection ->
//						connection.addHandlerLast(new ReadTimeoutHandler(100))
//								.addHandlerLast(new WriteTimeoutHandler(10)));
//		return HttpClient.from(tcpClient);
//	}
//
//	@Test
//	public void testMQ() throws InterruptedException {
//		ExecutorService rabbitmq_svr_thread = Executors.newSingleThreadExecutor();
//		final String QUEUE_NAME = "hello";
//		rabbitmq_svr_thread.submit(() -> {
//			ConnectionFactory factory = new ConnectionFactory();
//			factory.setHost("localhost");
//			factory.setPort(5672);
//
//			try (
//					com.rabbitmq.client.Connection connection = factory.newConnection();
//					Channel channel = connection.createChannel()
//			) {
//				channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//				for (int i = 0; i < 10; i++) {
//					String message = "Hello World! I am RabbitMQ! Number " + Math.round(Math.random() * 20);
//					channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//					System.out.println(" [x] Sent '" + message + "'");
//					TimeUnit.SECONDS.sleep(5);
//				}
//
//			} catch (Exception e) {
//				log.error(e.getMessage(),e);
//			}
//		});
//		TimeUnit.SECONDS.sleep(5);
//		ExecutorService rabbitmq_cln_thread = Executors.newSingleThreadExecutor();
//		rabbitmq_cln_thread.submit(() -> {
//			ConnectionFactory factory = new ConnectionFactory();
//			factory.setHost("localhost");
//			factory.setPort(5672);
//			try (
//					com.rabbitmq.client.Connection connection = factory.newConnection();
//					Channel channel = connection.createChannel()
//			) {
//				DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//					String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//					System.out.println(" [x] Received '" + message + "'");
//				};
//				channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
//				});
//			} catch (Exception e) {
//				log.error(e.getMessage(),e);
//			}
//		});
//		new Scanner(System.in).nextLine();
//	}
//
//	@Test
//	public void testMQ2() throws Exception {
//		OrdersJSON ordersJSON = new WebClientReactor().findFreeOrder(
//				"https://api.transport2.ru/carrier/graphql?operation=freeOrders",
//				"d52ffc2eb2261666abcad63f6072108597d83192")
//				.doOnError((error) -> {
//					log.error("doOnError: {}", error.getMessage());
//				})
//				.onErrorComplete()
//				.block();
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		com.rabbitmq.client.Connection connection = factory.newConnection();
//		Channel channel = connection.createChannel();
//		channel.exchangeDeclare("logs", "fanout");
//		String queueName = channel.queueDeclare().getQueue();
//		channel.queueBind(queueName, "logs", "");
//		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//			System.out.println(" [x] Received '" + message + "'");
//		};
//		channel.basicConsume("logs", true, deliverCallback, consumerTag -> { });
//		System.out.println(ordersJSON);
//		TimeUnit.SECONDS.sleep(10);
//	}
    @Test
    public void dbTest() throws SQLException, ParseException {
//        ResponseJsonToken jsonToken = new ResponseJsonToken();
//        jsonToken.setToken("eyJhbGciOiJIUzUxMiJ9.eyJhdHRlbXB0X2NvdW50IjowLCJhdHRlbXB0X2dyb3VwIjpudWxsLCJjYXB0Y2hhX2tleSI6InlzYzFfWDVSV0Y5Nms2aU5KOU91YXk0dDhiOTNsTTBwNFNFRmR0WURNY0hodzE3NWI3MzRjIiwiY2FwdGNoYV90eXBlIjoiWWFuZGV4IiwiY2xpZW50IjpudWxsLCJjbGllbnRfaWQiOiI2YTQ5OWI3ZS05MGExLTQxNzEtOWJhZS1kYjQ0YjY5ODBmYmQiLCJjb2RlX2xlbmd0aCI6NiwiY3JlYXRlZF9hdCI6IjIwMjQtMDgtMjNUMTU6MDk6MTguMjk0OTI1WiIsImNyZWRlbnRpYWxzIjoiYW5uYS5rQHNncHJvLnN1IiwiZGV2aWNlX2lkIjoiIiwiZGV2aWNlX25hbWUiOiIiLCJlbWFpbCI6ImFubmEua0BzZ3Byby5zdSIsImV4cCI6MTcyNDQyNjM1OCwiZXh0ZXJuYWxfaWQiOiIiLCJmaW5nZXJwcmludCI6ImFiMDAyMjI0ZGExZTc2ODMxNDkwN2UxOTY2ZWNhZGVkIiwiaGFzaCI6IiIsImlhdCI6MTcyNDQyNTc1OCwiaWRlbnRpZmllciI6ImFubmEua0BzZ3Byby5zdSIsImlwIjoiMTcyLjE4LjAuMTgiLCJsaW1pdF9yZXNldCI6NjAwLCJsb2NhbGUiOiJydV9SVSIsIm1lc3NhZ2UiOnt9LCJtZXRhIjp7InJlcXVlc3RlZF9zY29wZXMiOlsiZW1haWwucmVhZCIsInBob25lLnJlYWQiLCJwcm9maWxlX2luZm8ucmVhZCIsImF2YXRhci5yZWFkIiwiZW1wbG95ZWUucmVhZCJdLCJzY29wZXMiOlsiZW1haWwucmVhZCIsInBob25lLnJlYWQiLCJwcm9maWxlX2luZm8ucmVhZCIsImF2YXRhci5yZWFkIiwiZW1wbG95ZWUucmVhZCJdfSwib3MiOnsibmFtZSI6IiJ9LCJwbGF5ZXJfaWQiOm51bGwsInB1YmxpY19rZXkiOm51bGwsInB1YmxpY19rZXlfdHlwZSI6bnVsbCwicmVkaXJlY3RfdXJpIjoiIiwicmVwZWF0IjowLCJyZXN1bHQiOmZhbHNlLCJzZXJ2aWNlX2lkIjoiYjVmOTNiOWUtYzczMC00NzRlLTk2NjktN2U3YzdlNjZkMWE0Iiwic2Vzc2lvbl9pZCI6bnVsbCwic21zX3RlbXBsYXRlX3R5cGUiOm51bGwsInN1YiI6IjAiLCJzdWNjZWVkX2F0IjpudWxsLCJ2ZXJpZmllZF90b2tlbiI6bnVsbCwid2luX2FjYyI6IiJ9.l9OlLghe48SjUPGbc5NNH5Mr104SzKlk_hZVrnhvTRAJpqqf64Tw-BaWHCfTCFj4CbqbyGa-vs_kqWtenRkbiw");
//        jsonToken.setCode("670284");
//        System.out.println(new ObjectMapper().convertValue(jsonToken, Map.class));
//        webClientReactor.responseAcceptCodeMono(jsonToken);

//        GraphQLClient graphQLClient = new GraphQLClient();
//        Token token = tokenService.getToken(1L).block();
//        List<Cardtransport> cardtransports = cardTransportService.findAll().collectList().block();
//        if (cardtransports != null) {
//            List<Orders> orders = graphQLClient.freeOrders(token.getToken(), cardtransports.stream().map(Cardtransport::getPlaceofloading).toList());
//            System.out.println(orders);
//            if (orders != null && !orders.isEmpty()) {
//                sendService.sendFreeOrders("HTTPS: " + orders);
//            }
//        }


//        System.out.println(new HttpsHelpers().getToken(variablesProperties));

    }
//    @Test
//    public void teTest(){
//        WebClient webClient = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(Helpers.httpClient()))
//                .build();
//        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
//                .url("https://api.transport2.ru/carrier/graphql")
//                .headers(Helpers.getConsumerHeaders("dd25b7404a3a886d5208533673001d20e62e77e3"))
//                .build();
//
//        String doc = """
//                query AuctionOrders {
//                    auctionOrders(first: 1, loadingPlaceByFiasCodes: ["74000001", "86", "02046001", "42000014"], status: "FREE") {
//                        id
//                        unloadingDate
//                        loadingDate
//                        loadingDatetime
//                        unloadingDatetime
//                        externalNo
//                        loadingPlaces {
//                            storagePoint {
//                                settlement
//                            }
//                        }
//                    }
//                }
//                """;
//        try {
//            List<Orders> order = graphQlClient.document(Helpers.auctionNewOrdersGraphQL(List.of("74000001", "86", "02046001", "42000014")))
//                    .retrieveSync("auctionOrders")
//                    .toEntity(List.class);
//            System.out.println(order);
//        } catch (WebClientResponseException | GraphQlTransportException e) {
//            log.error(e.getMessage(), e);
//            System.out.println(tokenService.getToken(1L).block());
//        }
//
//
//    }
//    @Test
//    public void teTest2() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//        Date now = formatter.parse("2024-10-09T19:00:00+05:00");
//        String date = formatter.format(new Date());
//        System.out.println(now.getTime());
//        System.out.println(date);
//        long l_1 = 1728482400600L;
//        StringBuilder stringBuilder = new StringBuilder(String.valueOf(l_1));
//        stringBuilder.replace(stringBuilder.length() - 3, stringBuilder.length(), "000");
//        System.out.println(stringBuilder);
//        Date d_1 = new Date(Long.parseLong(stringBuilder.toString()));
//        Date d_2 = new Date(1728482400000L);
//        System.out.println(d_1.getTime() + " == " + d_2.getTime());
//        System.out.println(d_1.getTime() == d_2.getTime());
//    }
    @Test
    public void th() throws InterruptedException {
        ExecutorService service_1 = Executors.newSingleThreadExecutor();
        ExecutorService service_2 = Executors.newSingleThreadExecutor();
        ExecutorService service_3 = Executors.newSingleThreadExecutor();
        ExecutorService service_4 = Executors.newSingleThreadExecutor();
        List<String> list = List.of("02046001","74000001","42000014");
        WebClientReactor webClientReactor = new WebClientReactor();
        List<String> start = new ArrayList<>();
        List<String> end = new ArrayList<>();
        System.out.println(new JSONObject(Helpers.freeOrders));
        long startTime = System.currentTimeMillis();
//        HttpGraphQlClient graphQlClient1 = HttpGraphQlClient.builder(webClient())
//                .url("https://api.transport2.ru/carrier/graphql")
//                .headers(Helpers.getConsumerHeaders("db22b44f3751d52812d96a170004bb0b4c485408"))
//                .build();
//
//        System.out.println(graphQlClient1.document(Helpers.auctionNewOrdersGraphQL(list))
//                .retrieveSync("auctionOrders")
//                .toEntity(List.class));


        Runnable runnable_1 = () -> {
                String token = "db22b44f3751d52812d96a170004bb0b4c485408";
                long endTime = System.currentTimeMillis() - startTime;
                add(start,"Start: " + endTime + ": " + Thread.currentThread().getName());
//                System.out.println("Start: " + endTime + ": " + Thread.currentThread().getName() );
                Map<String, String> map = new HashMap<>();
                map.put("operationName", "auctionNewOrders");
                map.put("variables", "{  }");
                map.put("query", "query auctionNewOrders {\n  auctionOrders(first:10) {\n    id\n    externalNo\n   }}");

                HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient())
                        .url("https://api.transport2.ru/carrier/graphql")
                        .headers(Helpers.getConsumerHeaders(token))
                        .build();

                List<Orders> s = graphQlClient.document(Helpers.auctionNewOrdersGraphQL(list))
                        .retrieveSync("auctionOrders")
                        .toEntity(List.class);


//                s = RestClient.create()
//                        .post()
//                        .uri("https://api.transport2.ru/carrier/graphql?operation=auctionNewOrders")
//                        .headers(Helpers.getConsumerHeaders(token))
//                        .body(map)
//                        .retrieve()
//                        .onStatus(HttpStatusCode::is4xxClientError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .onStatus(HttpStatusCode::is5xxServerError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .toEntity(String.class).getBody();
                System.out.println("not proxy = " + s);


                endTime = System.currentTimeMillis() - startTime;
                add(end,"End: " + endTime + ": " + Thread.currentThread().getName() + " " + s);
//                    System.out.println("Finish: " + endTime + ": " + Thread.currentThread().getName() + " " + response.statusMessage() + " " + response.body());

            };

        Runnable runnable_2 = () -> {
                long endTime = System.currentTimeMillis() - startTime;
                add(start,"Start: " + endTime + ": " + Thread.currentThread().getName());
//                System.out.println("Start: " + endTime + ": " + Thread.currentThread().getName() );
                Map<String, String> map = new HashMap<>();
                map.put("operationName", "auctionNewOrders");
                map.put("variables", "{  }");
                map.put("query", "query auctionNewOrders {\n  auctionOrders(first:10) {\n    id\n    externalNo\n   }}");

                HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClientProxy("185.147.128.194", 8000,"JA7dG8", "9V4bxD"))
                        .url("https://api.transport2.ru/carrier/graphql")
                        .headers(Helpers.getConsumerHeaders("0de90fb5b63d00d8ba94dee558bad61659b1e70b"))
                        .build();

                List<Orders> s = graphQlClient.document(Helpers.auctionNewOrdersGraphQL(list))
                        .retrieveSync("auctionOrders")
                        .toEntity(List.class);

//                String s = httpsProxy("https://api.transport2.ru/carrier/graphql?operation=auctionNewOrders", "185.147.128.194", 8000,"JA7dG8", "9V4bxD").post()
//                        .headers(Helpers.getConsumerHeaders("0de90fb5b63d00d8ba94dee558bad61659b1e70b"))
//                        .body(map)
//                        .retrieve()
//                        .onStatus(HttpStatusCode::is4xxClientError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .onStatus(HttpStatusCode::is5xxServerError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .toEntity(String.class).getBody();
                System.out.println("proxy = " + s);
                endTime = System.currentTimeMillis() - startTime;
                add(end,"End: " + endTime + ": " + Thread.currentThread().getName() + " " + s);
//                    System.out.println("Finish: " + endTime + ": " + Thread.currentThread().getName() + " " + response.statusMessage() + " " + response.body());

            };

        Runnable runnable_3 = () -> {
                long endTime = System.currentTimeMillis() - startTime;
                add(start,"Start: " + endTime + ": " + Thread.currentThread().getName());
//                System.out.println("Start: " + endTime + ": " + Thread.currentThread().getName() );
                Map<String, String> map = new HashMap<>();
                map.put("operationName", "auctionNewOrders");
                map.put("variables", "{  }");
                map.put("query", "query auctionNewOrders {\n  auctionOrders(first:10) {\n    id\n    externalNo\n   }}");
                HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClientProxy("46.174.107.197", 64518,"rnugPw9d", "yCw3Tbzx"))
                        .url("https://api.transport2.ru/carrier/graphql")
                        .headers(Helpers.getConsumerHeaders("7a4191bcd8a3218c30b3b44356c03063408f8ad3"))
                        .build();

                List<Orders> s = graphQlClient.document(Helpers.auctionNewOrdersGraphQL(list))
                        .retrieveSync("auctionOrders")
                        .toEntity(List.class);
//                String s = httpsProxy("https://api.transport2.ru/carrier/graphql?operation=auctionNewOrders", "46.174.107.197", 64518,"rnugPw9d", "yCw3Tbzx").post()
//                        .headers(Helpers.getConsumerHeaders("7a4191bcd8a3218c30b3b44356c03063408f8ad3"))
//                        .body(map)
//                        .retrieve()
//                        .onStatus(HttpStatusCode::is4xxClientError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .onStatus(HttpStatusCode::is5xxServerError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .toEntity(String.class).getBody();
                System.out.println("proxy = " + s);
                endTime = System.currentTimeMillis() - startTime;
                add(end,"End: " + endTime + ": " + Thread.currentThread().getName() + " " + s);
//                    System.out.println("Finish: " + endTime + ": " + Thread.currentThread().getName() + " " + response.statusMessage() + " " + response.body());

            };

            Runnable runnable_4 = () -> {
                String token = "ed4aaa7bb5e720e1d5b9e27e1a929f94b9bce932";
                long endTime = System.currentTimeMillis() - startTime;
                add(start,"Start: " + endTime + ": " + Thread.currentThread().getName());
//                System.out.println("Start: " + endTime + ": " + Thread.currentThread().getName() );
                Map<String, String> map = new HashMap<>();
                map.put("operationName", "auctionNewOrders");
                map.put("variables", "{  }");
                map.put("query", "query auctionNewOrders {\n  auctionOrders(first:10) {\n    id\n    externalNo\n   }}");

                HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClientProxy("45.146.230.141", 63548,"rnugPw9d", "yCw3Tbzx"))
                        .url("https://api.transport2.ru/carrier/graphql")
                        .headers(Helpers.getConsumerHeaders(token))
                        .build();

                List<Orders> s = graphQlClient.document(Helpers.auctionNewOrdersGraphQL(list))
                        .retrieveSync("auctionOrders")
                        .toEntity(List.class);

//                s = httpsProxy("https://api.transport2.ru/carrier/graphql?operation=auctionNewOrders", "45.146.230.141", 63548,"rnugPw9d", "yCw3Tbzx").post()
//                        .headers(Helpers.getConsumerHeaders(token))
//                        .body(map)
//                        .retrieve()
//                        .onStatus(HttpStatusCode::is4xxClientError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .onStatus(HttpStatusCode::is5xxServerError,
//                                (req, res) ->
//                                        System.err.println("Couldn't delete " + res.getStatusText())
//                        )
//                        .toEntity(String.class).getBody();
                System.out.println("proxy = " + s);


                endTime = System.currentTimeMillis() - startTime;
                add(end,"End: " + endTime + ": " + Thread.currentThread().getName() + " " + s);
//                    System.out.println("Finish: " + endTime + ": " + Thread.currentThread().getName() + " " + response.statusMessage() + " " + response.body());

            };
        Thread.sleep(50);

        for (int i = 0; i < 20; i++) {
            Thread thread_1 = new Thread(runnable_1);
            Thread thread_2 = new Thread(runnable_2);
            Thread thread_3 = new Thread(runnable_3);
            Thread thread_4 = new Thread(runnable_4);

            thread_1.start();
            thread_1.join();
//            Thread.sleep(10);

            thread_2.start();
            thread_2.join();
//            Thread.sleep(10);

            thread_3.start();
            thread_3.join();
//            Thread.sleep(10);

            thread_4.start();
            thread_4.join();
//            Thread.sleep(10);





        }
        Thread.sleep(7000);
        start.forEach(System.out::println);
        end.forEach(System.out::println);

    }
    @Test
    public void testProxy() throws IOException {
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                System.out.println(getRequestingHost() + ":" + getRequestingPort());
                return new PasswordAuthentication("JA7dG8", "9V4bxD".toCharArray());
            }
        });

        try {
            Document document = Jsoup.connect("https://api.transport2.ru/web/expeditor/v1/auction/counters")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                .headers(Helpers.getHeaders("0de90fb5b63d00d8ba94dee558bad61659b1e70b"))
                .get();

            String text = document.body().wholeText();
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(Thread.currentThread().getName() + " " + response.statusMessage() + " " + response.body());
    }

    private synchronized void add(List<String> list, String string){
        list.add(string);
    }
    private RestClient httpsProxy(String uri, String host, int port, String username, String password) {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(username, password.toCharArray())
        );

        HttpHost proxy = new HttpHost(host, port);

        HttpClient httpClient = HttpClientBuilder
                .create()
                .setProxy(proxy)
                .setDefaultCredentialsProvider(credentialsProvider)
                .disableCookieManagement().build();



        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return RestClient
                .builder()
                .baseUrl(uri)
                .requestFactory(requestFactory)
                .build();
    }
    private WebClient webClient() {
       return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        Helpers.httpClient()))
                .build();
    }
    private WebClient webClientProxy(String host, int port, String username, String password) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        Helpers.httpClient().proxy(typeSpec -> typeSpec
                                .type(ProxyProvider.Proxy.HTTP)
                                .host(host)
                                .port(port)
                                .username(username)
                                .password(pass -> password)
                        )))
                .build();
    }
    private RestTemplate createRestTemplate() throws Exception {
        final String username = "JA7dG8";
        final String password = "9V4bxD";
        final String proxyUrl = "185.147.128.194";
        final int port = 8000;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        ((BasicCredentialsProvider) credsProvider).setCredentials(
                new AuthScope(proxyUrl, port),
                new UsernamePasswordCredentials(username, password.toCharArray())
        );

        HttpHost myProxy = new HttpHost(proxyUrl, port);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
    @Test
    public void testEst(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        try {
            for (int i = 0; i <= list.size(); i++) {
                System.out.println(list.get(i));
            }
        } catch (IndexOutOfBoundsException ignored) {
            log.error(ignored.getMessage(), ignored);
        }

    }

}

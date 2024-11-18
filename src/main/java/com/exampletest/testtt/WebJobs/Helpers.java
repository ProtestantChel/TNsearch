package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.models.FilterGraphQL;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandshakeTimeoutException;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Slf4j
public class Helpers {
    public static SslContext sslContext() {
        SslContext sslContext = null;
        try {

            sslContext = SslContextBuilder.forClient()
                    .protocols("SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2")
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

        } catch (SSLException ignored) {
        }
        return sslContext;
    }

    public static HttpClient httpClient() {
       return HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .secure(t -> t.sslContext(sslContext()))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(100))
                                .addHandlerLast(new WriteTimeoutHandler(10)));
    }

    public static String freeOrdersGraphQL(List<String> list){
       return  """
                query FreeOrders {
                    freeOrders(first: 100""" +
                (list != null && !list.isEmpty() ? ", loadingPlaceByFiasCodes: " + list.stream().map(m -> "\"" + m + "\"").toList() + " )" : ")") +
                """
                        {
                                id
                                unloadingDate
                                loadingDate
                                loadingDatetime
                                unloadingDatetime
                                externalNo
                                status
                                loadingPlaces {
                                    storagePoint {
                                        settlement
                                        fiasCode
                                    }
                                }
                            }
                        }
                        """;
    }
    public static String auctionNewOrdersGraphQL(List<String> list){
        int random = ThreadLocalRandom.current().nextInt(1, 100);
//        System.out.println("thread name = " + Thread.currentThread().getName() + ", random: " + random);
        return  """
                query AuctionOrders {
                    auctionOrders(""" + "first: " + random + ", status: \"FREE\"" +
                (list != null && !list.isEmpty() ? ", loadingPlaceByFiasCodes: " + list.stream().map(m -> "\"" + m + "\"").toList() + " )" : ")") +
                """
                        {
                                id
                                unloadingDate
                                loadingDate
                                loadingDatetime
                                unloadingDatetime
                                externalNo
                                status
                                loadingPlaces {
                                    storagePoint {
                                        settlement
                                    }
                                }
                            }
                        }
                        """;
    }
    public static String bookOrderGraphQL(String id){
        return  """
                mutation BookOrder {
                    bookOrder(orderId:""" + id + ")" +
                """
                    {
                        status
                        id
                      }
                    }
                """;
    }
    public static final Map<String, String> freeOrders = Map.of(
            "operationName", "freeOrders",
             "variables", """ 
                      {
                          "totalCount": true,
                          "showTransport": false,
                          "loadingPlaceByFiasCodes": [],
                          "unloadingPlaceByFiasCodes": [],
                          "loadingStatuses": null,
                          "first": 20,
                          "offset": 0,
                          "sortBy": ["is_shipment_on_agreed_date_and_status_not_completed", "-id"]"
                      }
                    }""",
                "query", "query freeOrders($first: Int, $offset: Int, $totalCount: Boolean = false, $showTransport: Boolean = true, $loadingPlaceByFiasCodes: [String], $unloadingPlaceByFiasCodes: [String], $actualProblemEvent: ActualProblemEventEnum, $loadingDates: [Date], $unloadingDates: [Date], $loadingDateFrom: Date, $loadingDateTo: Date, $unloadingDateFrom: Date, $unloadingDateTo: Date, $externalNo: ID, $id: ID, $driverDataFullname: String, $vehicleDataRegNo: String, $isPreviouslyAssigned: Boolean, $deliveryContractNumber: String, $sortBy: [String]) {\n" +
                    "  freeOrders(\n" +
                    "    first: $first\n" +
                    "    offset: $offset\n" +
                    "    loadingPlaceByFiasCodes: $loadingPlaceByFiasCodes\n" +
                    "    unloadingPlaceByFiasCodes: $unloadingPlaceByFiasCodes\n" +
                    "    actualProblemEvent: $actualProblemEvent\n" +
                    "    loadingDates: $loadingDates\n" +
                    "    unloadingDates: $unloadingDates\n" +
                    "    loadingDateFrom: $loadingDateFrom\n" +
                    "    loadingDateTo: $loadingDateTo\n" +
                    "    unloadingDateFrom: $unloadingDateFrom\n" +
                    "    unloadingDateTo: $unloadingDateTo\n" +
                    "    externalNo: $externalNo\n" +
                    "    id: $id\n" +
                    "    driverDataFullname: $driverDataFullname\n" +
                    "    vehicleDataRegNo: $vehicleDataRegNo\n" +
                    "    isPreviouslyAssigned: $isPreviouslyAssigned\n" +
                    "    deliveryContractNumber: $deliveryContractNumber\n" +
                    "    sortBy: $sortBy\n" +
                    "  ) {\n" +
                    "    id\n" +
                    "    externalNo\n" +
                    "    loadingPlaces {\n" +
                    "      department\n" +
                    "      storagePoint {\n" +
                    "        companyName\n" +
                    "        settlement\n" +
                    "        name\n" +
                    "        address\n" +
                    "        id\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    unloadingPlaces {\n" +
                    "      storagePoint {\n" +
                    "        companyName\n" +
                    "        settlement\n" +
                    "        name\n" +
                    "        address\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    loadingTypes\n" +
                    "    volume\n" +
                    "    status\n" +
                    "    isVatIncluded\n" +
                    "    loadingDatetime\n" +
                    "    unloadingDatetime\n" +
                    "    price\n" +
                    "    priceWithoutVat\n" +
                    "    priceWithVat\n" +
                    "    weight\n" +
                    "    comment\n" +
                    "    isMarket\n" +
                    "    viewEndDatetime\n" +
                    "    secondsToLifeTimeExpired\n" +
                    "    secondsToReservationTimeExpired\n" +
                    "    customerOrganization {\n" +
                    "      name\n" +
                    "      isTn\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    signingWay\n" +
                    "    vehicleRequirements {\n" +
                    "      name\n" +
                    "      bodySubtype {\n" +
                    "        name\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    isShipmentOnAgreedDate\n" +
                    "    isPreviouslyAssigned\n" +
                    "    viewProcessDeadline\n" +
                    "    gracePeriod\n" +
                    "    diy\n" +
                    "    orderAttributes {\n" +
                    "      firstGoodsName\n" +
                    "      goodsCount\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    priceViewTn\n" +
                    "    isDeadlineSuspended\n" +
                    "    isEshop\n" +
                    "    country\n" +
                    "    statusOnEvent\n" +
                    "    signDeadline\n" +
                    "    documentActualStatus {\n" +
                    "      status\n" +
                    "      id\n" +
                    "      documentType {\n" +
                    "        innerName\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    documentStatusHistory {\n" +
                    "      status\n" +
                    "      documentType {\n" +
                    "        innerName\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    driver @include(if: $showTransport) {\n" +
                    "      surname\n" +
                    "      name\n" +
                    "      patronymic\n" +
                    "      phone\n" +
                    "      addPhone\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    vehicle @include(if: $showTransport) {\n" +
                    "      regNo\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    tender {\n" +
                    "      lotNumber\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    deliveryContractNumber\n" +
                    "    sourceOrder {\n" +
                    "      id\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    assigningReason\n" +
                    "    routeSheet {\n" +
                    "      actualProblemEvents {\n" +
                    "        event\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      isDriverOnline\n" +
                    "      events {\n" +
                    "        id\n" +
                    "        event\n" +
                    "        storagePoint {\n" +
                    "          id\n" +
                    "          __typename\n" +
                    "        }\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    __typename\n" +
                    "  }\n" +
                    "  totalCount: freeOrdersCount(\n" +
                    "    loadingPlaceByFiasCodes: $loadingPlaceByFiasCodes\n" +
                    "    unloadingPlaceByFiasCodes: $unloadingPlaceByFiasCodes\n" +
                    "    actualProblemEvent: $actualProblemEvent\n" +
                    "    loadingDates: $loadingDates\n" +
                    "    unloadingDates: $unloadingDates\n" +
                    "    loadingDateFrom: $loadingDateFrom\n" +
                    "    loadingDateTo: $loadingDateTo\n" +
                    "    unloadingDateFrom: $unloadingDateFrom\n" +
                    "    unloadingDateTo: $unloadingDateTo\n" +
                    "    externalNo: $externalNo\n" +
                    "    id: $id\n" +
                    "    driverDataFullname: $driverDataFullname\n" +
                    "    vehicleDataRegNo: $vehicleDataRegNo\n" +
                    "    deliveryContractNumber: $deliveryContractNumber\n" +
                    "    sortBy: $sortBy\n" +
                    "  ) @include(if: $totalCount)\n" +
                    "}"
    );
    public static Map<String,String> assignedOrders = Map.of(
            "operationName", "assignedOrders",
            "variables", "{\n" +
                    "  \"totalCount\": true,\n" +
                    "  \"showTransport\": false,\n" +
                    "  \"loadingPlaceByFiasCodes\": [],\n" +
                    "  \"unloadingPlaceByFiasCodes\": [],\n" +
                    "  \"status\": \"ASSIGNED\",\n" +
                    "  \"first\": 1000,\n" +
                    "  \"offset\": 0,\n" +
                    "  \"sortBy\": [\n" +
                    "    \"is_shipment_on_agreed_date_and_status_not_completed\",\n" +
                    "    \"-id\"\n" +
                    "  ]\n" +
                    "}",
            "query", "query assignedOrders($status: String, $statuses: [String], $first: Int, $offset: Int, $totalCount: Boolean = false, $showTransport: Boolean = true, $loadingPlaceByFiasCodes: [String], $unloadingPlaceByFiasCodes: [String], $actualProblemEvent: ActualProblemEventEnum, $loadingDates: [Date], $unloadingDates: [Date], $loadingDateFrom: Date, $loadingDateTo: Date, $unloadingDateFrom: Date, $unloadingDateTo: Date, $externalNo: ID, $id: ID, $deliveryContractNumber: String, $driverDataFullname: String, $vehicleDataRegNo: String, $sortBy: [String]) {\n" +
                    "  assignedOrders(\n" +
                    "    status: $status\n" +
                    "    statuses: $statuses\n" +
                    "    first: $first\n" +
                    "    offset: $offset\n" +
                    "    loadingPlaceByFiasCodes: $loadingPlaceByFiasCodes\n" +
                    "    unloadingPlaceByFiasCodes: $unloadingPlaceByFiasCodes\n" +
                    "    actualProblemEvent: $actualProblemEvent\n" +
                    "    loadingDates: $loadingDates\n" +
                    "    unloadingDates: $unloadingDates\n" +
                    "    loadingDateFrom: $loadingDateFrom\n" +
                    "    loadingDateTo: $loadingDateTo\n" +
                    "    unloadingDateFrom: $unloadingDateFrom\n" +
                    "    unloadingDateTo: $unloadingDateTo\n" +
                    "    externalNo: $externalNo\n" +
                    "    id: $id\n" +
                    "    driverDataFullname: $driverDataFullname\n" +
                    "    vehicleDataRegNo: $vehicleDataRegNo\n" +
                    "    deliveryContractNumber: $deliveryContractNumber\n" +
                    "    sortBy: $sortBy\n" +
                    "  ) {\n" +
                    "    id\n" +
                    "    externalNo\n" +
                    "    loadingPlaces {\n" +
                    "      department\n" +
                    "      storagePoint {\n" +
                    "        name\n" +
                    "        address\n" +
                    "        id\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    unloadingPlaces {\n" +
                    "      storagePoint {\n" +
                    "        name\n" +
                    "        address\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    loadingTypes\n" +
                    "    volume\n" +
                    "    status\n" +
                    "    isVatIncluded\n" +
                    "    loadingDatetime\n" +
                    "    unloadingDatetime\n" +
                    "    price\n" +
                    "    priceWithoutVat\n" +
                    "    priceWithVat\n" +
                    "    weight\n" +
                    "    temperatureMode\n" +
                    "    comment\n" +
                    "    allocationType\n" +
                    "    isMarket\n" +
                    "    statusOnEvent\n" +
                    "    viewEndDatetime\n" +
                    "    secondsToLifeTimeExpired\n" +
                    "    secondsToReservationTimeExpired\n" +
                    "    customerOrganization {\n" +
                    "      name\n" +
                    "      isTn\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    signingWay\n" +
                    "    documentActualStatus {\n" +
                    "      status\n" +
                    "      id\n" +
                    "      documentType {\n" +
                    "        innerName\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    documentStatusHistory {\n" +
                    "      status\n" +
                    "      documentType {\n" +
                    "        innerName\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    vehicleRequirements {\n" +
                    "      name\n" +
                    "      bodySubtype {\n" +
                    "        name\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    driver @include(if: $showTransport) {\n" +
                    "      surname\n" +
                    "      name\n" +
                    "      patronymic\n" +
                    "      phone\n" +
                    "      addPhone\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    vehicle @include(if: $showTransport) {\n" +
                    "      regNo\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    isShipmentOnAgreedDate\n" +
                    "    isPreviouslyAssigned\n" +
                    "    viewProcessDeadline\n" +
                    "    gracePeriod\n" +
                    "    tender {\n" +
                    "      lotNumber\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    diy\n" +
                    "    orderAttributes {\n" +
                    "      firstGoodsName\n" +
                    "      goodsCount\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    deliveryContractNumber\n" +
                    "    sourceOrder {\n" +
                    "      id\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    priceViewTn\n" +
                    "    isDeadlineSuspended\n" +
                    "    isEshop\n" +
                    "    country\n" +
                    "    routeSheet {\n" +
                    "      actualProblemEvents {\n" +
                    "        event\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      isDriverOnline\n" +
                    "      events {\n" +
                    "        id\n" +
                    "        event\n" +
                    "        storagePoint {\n" +
                    "          id\n" +
                    "          __typename\n" +
                    "        }\n" +
                    "        __typename\n" +
                    "      }\n" +
                    "      __typename\n" +
                    "    }\n" +
                    "    __typename\n" +
                    "  }\n" +
                    "  totalCount: assignedOrdersCount(\n" +
                    "    status: $status\n" +
                    "    statuses: $statuses\n" +
                    "    loadingPlaceByFiasCodes: $loadingPlaceByFiasCodes\n" +
                    "    unloadingPlaceByFiasCodes: $unloadingPlaceByFiasCodes\n" +
                    "    actualProblemEvent: $actualProblemEvent\n" +
                    "    loadingDates: $loadingDates\n" +
                    "    unloadingDates: $unloadingDates\n" +
                    "    loadingDateFrom: $loadingDateFrom\n" +
                    "    loadingDateTo: $loadingDateTo\n" +
                    "    unloadingDateFrom: $unloadingDateFrom\n" +
                    "    unloadingDateTo: $unloadingDateTo\n" +
                    "    externalNo: $externalNo\n" +
                    "    id: $id\n" +
                    "    deliveryContractNumber: $deliveryContractNumber\n" +
                    "    driverDataFullname: $driverDataFullname\n" +
                    "    vehicleDataRegNo: $vehicleDataRegNo\n" +
                    "    sortBy: $sortBy\n" +
                    "  ) @include(if: $totalCount)\n" +
                    "}\n"
    );
    public static Map<String,String> getHeaders(String token){
        return Map.of(
                "Accept", "application/json, text/plain, */*",
                "Referer", "https://tms.transport2.ru/",
                "Authorization", "Token " + token
        );
    }

    public static Consumer<HttpHeaders> getConsumerHeaders(String token){
        return httpHeaders -> httpHeaders.setAll(Map.of(
                "Accept", "application/json, text/plain, */*",
                "Referer", "https://tms.transport2.ru/",
                "Authorization", "Token " + token
        ));
    }

}

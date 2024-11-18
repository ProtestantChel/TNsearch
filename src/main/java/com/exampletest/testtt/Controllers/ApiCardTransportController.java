package com.exampletest.testtt.Controllers;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Service.CardTransportService;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.WebJobs.WebClientReactor;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.search.orders.AutoComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ctc")
public class ApiCardTransportController {
    @Autowired
    CardTransportService cardTransportService;
    @Autowired
    WebClientReactor webClientReactor;
    @Autowired
    TokenService tokenService;
    @Autowired
    DataMemory dataMemory;

    @GetMapping("/list")
    public Flux<Cardtransport> getCardTransport() {
        return cardTransportService.findAll();
    }
    @GetMapping("/cards/{id}")
    public Mono<Cardtransport> getCardTransportById(@PathVariable Long id) {
        return cardTransportService.findById(id);
    }
    @CrossOrigin(allowedHeaders = "*")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Cardtransport> saveCardTransport(@RequestBody Cardtransport cardTransport) {
        return cardTransportService.add(cardTransport);
    }
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Cardtransport> updateCardTransport(@RequestBody Cardtransport cardTransport) {
        return cardTransportService.update(cardTransport);
    }

    @PostMapping(value = "/deleteOne", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> deleteOneCardTransport(@RequestBody Cardtransport cardTransport) {
        List<Cardtransport> cardtransports = dataMemory.getCardtransports();
        dataMemory.setCardtransports(cardtransports.stream().filter(f -> !Objects.equals(f.getId(), cardTransport.getId())).toList());
        return cardTransportService.deleteById(cardTransport.getId()).then(Mono.defer(() -> Mono.just("success")));
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> deleteCardTransport(@RequestBody List<Long> ids) {
        List<Cardtransport> cardtransports = dataMemory.getCardtransports();
        dataMemory.setCardtransports(cardtransports.stream().filter(f -> !ids.contains(f.getId())).toList());
        return cardTransportService.deleteAllById(ids).then(Mono.defer(() -> Mono.just("success")));
    }

    @PostMapping(value = "/copy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Cardtransport> copyCardTransport(@RequestBody Cardtransport cardTransport) {
        List<Cardtransport> cardtransports = dataMemory.getCardtransports();
        cardtransports.add(cardTransport);
        dataMemory.setCardtransports(cardtransports);
        return cardTransportService.findById(cardTransport.getId()).flatMap(cpt -> {
            cpt.setId(null);
            return cardTransportService.add(cpt);
        });
    }

    @GetMapping("/fias/{values}")
    public Mono<List<AutoComplete>> getFias(@PathVariable String values) {
        return tokenService.getToken()
                .flatMap(m -> webClientReactor.findFias("https://api.transport2.ru/fias/suggest/" + values, m.getToken()))
                .flatMap(m -> Mono.just(m.getAddresses().stream().map(mm -> new AutoComplete(mm.getFull_address(), mm.getPlaincode())).toList()));
    }




}

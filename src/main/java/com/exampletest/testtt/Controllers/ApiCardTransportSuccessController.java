package com.exampletest.testtt.Controllers;

import com.exampletest.testtt.Service.CardTransportSuccessService;
import com.exampletest.testtt.models.tms.Entity.SuccesscardTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/ctsc")
public class ApiCardTransportSuccessController {

    @Autowired
    CardTransportSuccessService cardTransportSuccessService;

    @PostMapping(value = "/addAll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<SuccesscardTransport> insertSearchCardTransport(@RequestBody List<SuccesscardTransport> cardtransportsuccesses) {
        return cardTransportSuccessService.saveAll(cardtransportsuccesses);
    }

    @GetMapping("/list")
    public Flux<SuccesscardTransport> getList() {
        return cardTransportSuccessService.findAll();
    }

}

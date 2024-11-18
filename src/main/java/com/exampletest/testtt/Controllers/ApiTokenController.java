package com.exampletest.testtt.Controllers;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.models.tms.Entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/token")
public class ApiTokenController {
    @Autowired
    TokenService tokenService;
    @Autowired
    DataMemory dataMemory;

    @GetMapping("/update/{token}")
    public Mono<Token> setToken(@PathVariable String token) {
        Token tokenObj = new Token();
        tokenObj.setToken(token);
        return tokenService.setToken(tokenObj);
    }
    @GetMapping("/show")
    public Mono<Token> getToken() {
        return tokenService.getToken();
    }

    @GetMapping("/get/{token}")
    public Mono<Token> getFindByToken(@PathVariable String token) {
        return tokenService.findByToken(token);
    }

    @GetMapping("/gh")
    public Mono<String> gh() {

        return Mono.just("OK");
    }
}

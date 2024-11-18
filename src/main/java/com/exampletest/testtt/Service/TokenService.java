package com.exampletest.testtt.Service;

import com.exampletest.testtt.models.tms.Entity.Token;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TokenService {
    Mono<Token> getToken();
    Mono<Token> setToken(Token token);
    Flux<Token> getTokens();
    Mono<Token> findByToken(String token);
    Mono<Void> delete(String token);
    Mono<Long> maxId();
}

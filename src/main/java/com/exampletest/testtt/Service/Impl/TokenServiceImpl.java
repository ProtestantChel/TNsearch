package com.exampletest.testtt.Service.Impl;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Repositories.TmsRepository.TokenRepository;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.Entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DataMemory dataMemory;


    @Override
    public Mono<Token> getToken() {
        return tokenRepository.getToken();
    }

    @Override
    public Mono<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Mono<Token> setToken(Token token) {
        if (token.getId() == null)
            return tokenRepository.save(token);
        return tokenRepository.maxId().flatMap(f -> {
            token.setId(f+1);
            dataMemory.setToken(token);
            return tokenRepository.save(token);
        });
    }

    @Override
    public Flux<Token> getTokens() {
        return tokenRepository.findAll();
    }

    @Override
    public Mono<Void> delete(String token) {
        return tokenRepository.delete(token);
    }

    @Override
    public Mono<Long> maxId(){
        return tokenRepository.maxId();
    }

}

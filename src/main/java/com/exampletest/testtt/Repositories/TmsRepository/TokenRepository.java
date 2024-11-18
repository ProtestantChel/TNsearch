package com.exampletest.testtt.Repositories.TmsRepository;


import com.exampletest.testtt.models.tms.Entity.Token;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TokenRepository extends ReactiveCrudRepository<Token, Long> {
    @Query("SELECT id, token FROM token WHERE token=:token")
    Mono<Token> findByToken(@Param("token") String token);

    @Query("SELECT id from token ORDER BY id DESC LIMIT 1")
    Mono<Long> maxId();

    @Query("SELECT id, token FROM token ORDER BY id ASC limit 1")
    Mono<Token> getToken();

    @Query("DELETE FROM token WHERE token=:token")
    Mono<Void> delete(@Param("token") String token);
}

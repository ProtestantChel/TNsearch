package com.exampletest.testtt.Repositories.TmsRepository;

import com.exampletest.testtt.models.tms.Entity.Cityasfias;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CityAsFiasRepository extends ReactiveCrudRepository<Cityasfias, Long> {
    @Query("SELECT city, fias FROM cityasfias WHERE city=:city")
    Mono<Cityasfias> findByFisa(String city);
}

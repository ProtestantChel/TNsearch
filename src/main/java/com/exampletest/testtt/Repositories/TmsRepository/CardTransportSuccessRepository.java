package com.exampletest.testtt.Repositories.TmsRepository;

import com.exampletest.testtt.models.tms.Entity.SuccesscardTransport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface CardTransportSuccessRepository extends ReactiveCrudRepository<SuccesscardTransport, Long> {
    @Query("DELETE FROM successcard_transport WHERE GUID IN (:guids)")
    Mono<Void> deleteByGuidIn(Set<String> guids);

}

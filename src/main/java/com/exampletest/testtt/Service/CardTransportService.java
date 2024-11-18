package com.exampletest.testtt.Service;

import com.exampletest.testtt.models.tms.Entity.SuccesscardTransport;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CardTransportService {
    Mono<Cardtransport> add(Cardtransport cardTransport);
    Mono<Cardtransport> update(Cardtransport cardTransport);
    Mono<Cardtransport> findById(Long id);
    Flux<Cardtransport> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Void> deleteAllById(List<Long> ids);
    Mono<Long> maxId();
}

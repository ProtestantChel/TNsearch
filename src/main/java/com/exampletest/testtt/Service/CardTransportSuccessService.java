package com.exampletest.testtt.Service;


import com.exampletest.testtt.models.tms.Entity.SuccesscardTransport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface CardTransportSuccessService {
    Flux<SuccesscardTransport> saveAll(List<SuccesscardTransport> successFlux);
    Flux<SuccesscardTransport> findAll();
    Mono<SuccesscardTransport> add(SuccesscardTransport successcardTransport);
    Mono<Void> deleteByGuidIn(Set<String> guids);
}

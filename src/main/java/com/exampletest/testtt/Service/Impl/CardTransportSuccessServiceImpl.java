package com.exampletest.testtt.Service.Impl;

import com.exampletest.testtt.Repositories.TmsRepository.CardTransportSuccessRepository;
import com.exampletest.testtt.Service.CardTransportSuccessService;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.Entity.SuccesscardTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Service
public class CardTransportSuccessServiceImpl implements CardTransportSuccessService {
    @Autowired
    CardTransportSuccessRepository cardTransportSuccessRepository;

    @Override
    public Flux<SuccesscardTransport> saveAll(List<SuccesscardTransport> successFlux) {
        return cardTransportSuccessRepository.saveAll(successFlux);
    }

    @Override
    public Flux<SuccesscardTransport> findAll() {
        return cardTransportSuccessRepository.findAll();
    }

    @Override
    public Mono<SuccesscardTransport> add(SuccesscardTransport successcardTransport) {
        return cardTransportSuccessRepository.save(successcardTransport);
    }
    @Override
    public Mono<Void> deleteByGuidIn(Set<String> guids) {
        return cardTransportSuccessRepository.deleteByGuidIn(guids);
    }
}

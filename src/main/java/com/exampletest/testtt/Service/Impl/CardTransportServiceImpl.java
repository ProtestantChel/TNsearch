package com.exampletest.testtt.Service.Impl;

import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Repositories.TmsRepository.CardTransportRepository;
import com.exampletest.testtt.Repositories.TmsRepository.CardTransportSuccessRepository;
import com.exampletest.testtt.Repositories.TmsRepository.CityAsFiasRepository;
import com.exampletest.testtt.Service.CardTransportService;
import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CardTransportServiceImpl implements CardTransportService {


    @Autowired
    CardTransportRepository cardTransportRepository;

    @Autowired
    CardTransportSuccessRepository cardTransportSuccessRepository;

    @Autowired
    CityAsFiasRepository cityAsFiasRepository;

    @Autowired
    DataMemory dataMemory;


    @Override
    public Mono<Cardtransport> add(Cardtransport cardTransport) {
        if (cardTransport.getId() == null) {
            return cardTransportRepository.maxId().flatMap(f -> {
                List<Cardtransport> cardtransports = new ArrayList<>(dataMemory.getCardtransports());
                cardTransport.setId(f+1);
                cardtransports.add(cardTransport);
                dataMemory.setCardtransports(cardtransports);
                cardTransport.setId(null);
                return cardTransportRepository.save(cardTransport);
            });
        } else {
            List<Cardtransport> cardtransports = dataMemory.getCardtransports();
            dataMemory.setCardtransports(cardtransports.stream().map(m -> Objects.equals(m.getId(), cardTransport.getId()) ? cardTransport : m).toList());
            return cardTransportRepository.findById(cardTransport.getId())
                    .flatMap(ctp -> cardTransportRepository.save(cardTransport));
        }
    }
    @Override
    public Mono<Cardtransport> update(Cardtransport cardTransport) {
        return cardTransportRepository.findById(cardTransport.getId())
                .flatMap(ctp -> cardTransportRepository.save(cardTransport));
    }
    @Override
    public Mono<Void> deleteById(Long id) {
        return cardTransportRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAllById(List<Long> ids) {
        return cardTransportRepository.deleteAllById(ids);
    }

    @Override
    public Mono<Cardtransport> findById(Long id) {
        return id != null ? cardTransportRepository.findById(id) : Mono.empty();
    }

    @Override
    public Flux<Cardtransport> findAll() {
        return cardTransportRepository.findAll();
    }

    @Override
    public Mono<Long> maxId(){
        return cardTransportRepository.maxId();
    }

}

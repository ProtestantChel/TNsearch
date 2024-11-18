package com.exampletest.testtt.Service.Impl;


import com.exampletest.testtt.Repositories.TmsRepository.CityAsFiasRepository;

import com.exampletest.testtt.Service.CityAsFiasService;

import com.exampletest.testtt.models.tms.Entity.Cityasfias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;


@Service
public class CityAsFiasServiceImpl implements CityAsFiasService {


    @Autowired
    CityAsFiasRepository cityAsFiasRepository;

    @Override
    public Mono<Cityasfias> add(Cityasfias cardTransport) {
        return cityAsFiasRepository.save(cardTransport);
    }

    @Override
    public Mono<Cityasfias> findByFias(String city) {
        return cityAsFiasRepository.findByFisa(city);
    }
}

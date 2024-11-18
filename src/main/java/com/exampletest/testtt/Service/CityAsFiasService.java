package com.exampletest.testtt.Service;

import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.Entity.Cityasfias;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CityAsFiasService {
    Mono<Cityasfias> add(Cityasfias cityasfias);
    Mono<Cityasfias> findByFias(String city);
}

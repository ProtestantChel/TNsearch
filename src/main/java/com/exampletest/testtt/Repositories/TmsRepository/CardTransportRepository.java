package com.exampletest.testtt.Repositories.TmsRepository;

import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CardTransportRepository extends ReactiveCrudRepository<Cardtransport, Long> {
    @Query("SELECT id from cardtransport ORDER BY id DESC LIMIT 1")
    Mono<Long> maxId();

//    @Modifying
//    @Query("UPDATE cardtransport SET placeofloading=:#{#cardtransport.placeofloading}, placeofloadingfias=:#{#cardtransport.placeofloading}, placeofdelivery=:#{#cardtransport.placeofloading}, onplaceofdelivery=:#{#cardtransport.placeofloading}, shipmentstart=:#{#cardtransport.placeofloading}, onnum=:#{#cardtransport.placeofloading}, chk_apply=:#{#cardtransport.placeofloading}, chk_search=:#{#cardtransport.placeofloading}, send_ati=:#{#cardtransport.placeofloading}, continuingsearch=:#{#cardtransport.placeofloading} WHERE id =")
//    Mono<Boolean> updateCard(Cardtransport cardtransport);
}

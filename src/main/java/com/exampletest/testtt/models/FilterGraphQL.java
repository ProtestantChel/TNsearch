package com.exampletest.testtt.models;

import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@NoArgsConstructor
public class FilterGraphQL {
    List<String> cityFias;
    String date;

    public FilterGraphQL(List<Cardtransport> cardtransports) {
        this.cityFias = cardtransports.stream().map(Cardtransport::getPlaceofloading).toList();
    }
}

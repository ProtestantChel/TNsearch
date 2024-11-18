package com.exampletest.testtt.orders;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String id;
    private String unloadingDate;
    private String loadingDate;
    private String loadingDatetime;
    private String unloadingDatetime;
    private String externalNo;
    private List<LoadingPlace> loadingPlaces;

}

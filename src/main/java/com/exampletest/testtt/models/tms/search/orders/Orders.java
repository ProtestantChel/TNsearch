package com.exampletest.testtt.models.tms.search.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Orders implements Serializable {
    private String externalNo;
    private Integer gracePeriod;
    private String id;
    private Boolean isMarket;
    private Boolean isPreviouslyAssigned;
    private Boolean isShipmentOnAgreedDate;
    private Boolean isVatIncluded;
    private String loadingDatetime;
    private List<LoadingPlaces> loadingPlaces;
    private String loadingTypes;
    private OrderAttributes orderAttributes;
    private Integer price;
    private Float priceViewTn;
    private Float priceWithVat;
    private Float priceWithoutVat;
    private Integer secondsToLifeTimeExpired;
    private String signingWay;
    private String status;
    private String volume;
    private String weight;
    private String unloadingDatetime;
    private List<UnloadingPlaces> unloadingPlaces;
    private VehicleRequirements vehicleRequirements;
    private String viewEndDatetime;

}

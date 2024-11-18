package com.exampletest.testtt.models.tms.Entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Cardtransport {
    @Id
    private Long id;
    private String placeofloading;
    private String placeofloadingfias;
    private String placeofdelivery;
    private String onplaceofdelivery;
    private Long shipmentstart;
    private String onnum;
    private Boolean chk_apply;
    private Boolean chk_search;
    private Boolean send_ati;
    private Boolean continuingsearch;

}

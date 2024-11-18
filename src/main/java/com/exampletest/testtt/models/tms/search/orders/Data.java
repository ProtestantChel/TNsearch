package com.exampletest.testtt.models.tms.search.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class Data {
    private Integer totalCount;
    private List<Orders> freeOrders;
}

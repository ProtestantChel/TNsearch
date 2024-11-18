package com.exampletest.testtt.models.tms.search.orders;

import lombok.Data;

import java.util.List;

@Data
public class FiasRequest {
    private Integer status_code;
    private List<Addresses> addresses;
}

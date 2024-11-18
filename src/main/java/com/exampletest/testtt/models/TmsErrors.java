package com.exampletest.testtt.models;

import lombok.Data;

@Data
public class TmsErrors {
    private Object error_fields;
    private String code;
    private String message;
}

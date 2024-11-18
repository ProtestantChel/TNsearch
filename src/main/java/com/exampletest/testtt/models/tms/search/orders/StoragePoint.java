package com.exampletest.testtt.models.tms.search.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class StoragePoint implements Serializable {
    private String id;
    private String name;
    private String address;
    private String settlement;
    private String fiasCode;
}

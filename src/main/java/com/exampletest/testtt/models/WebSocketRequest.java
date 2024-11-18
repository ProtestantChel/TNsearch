package com.exampletest.testtt.models;

import lombok.Data;

@Data
public class WebSocketRequest {
    private String topic;
    private String action;
    private Payload payload;

}

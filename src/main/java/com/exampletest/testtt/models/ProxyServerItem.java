package com.exampletest.testtt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyServerItem {
    private String host;
    private int port;
    private String username;
    private String password;
}

package com.exampletest.testtt.WebJobs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ResponseJsonToken {
    private String code;
    private String token;
}

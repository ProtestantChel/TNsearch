package com.exampletest.testtt.models.tms.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class SuccesscardTransport {

    private Integer id;
    private String guid;
    private Integer chk;
}

package com.exampletest.testtt.models.tms.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Cityasfias {
    private String city;
    private String fias;
}

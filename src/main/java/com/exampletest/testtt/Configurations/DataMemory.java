package com.exampletest.testtt.Configurations;

import com.exampletest.testtt.models.tms.Entity.Cardtransport;
import com.exampletest.testtt.models.tms.Entity.Token;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class DataMemory {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Token token;

    private List<Cardtransport> cardtransports;
    private List<Token> tokens;

    public void setToken(Token token) {
        if (tokens != null && tokens.stream().noneMatch(t -> t.getToken().equals(token.getToken()))) {
            tokens.add(token);
        }
    }

}

package com.barbearia.pagamentos.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "general")
public class AppConfigs {

    private String acceptedTokens;


    public void setAcceptedTokens(String acceptedTokens) {
        this.acceptedTokens = acceptedTokens;
    }
    public List<String> getAcceptedTokens() {
        return Arrays.asList(acceptedTokens.split(","));
    }
}

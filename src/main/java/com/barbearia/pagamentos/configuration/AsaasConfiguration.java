package com.barbearia.pagamentos.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "asaas")
public class AsaasConfiguration {

    private String acessToken;

    public String getacessToken() {
        return acessToken;
    }

    public void setacessToken(String uploadDir) {
        this.acessToken = uploadDir;
    }
}

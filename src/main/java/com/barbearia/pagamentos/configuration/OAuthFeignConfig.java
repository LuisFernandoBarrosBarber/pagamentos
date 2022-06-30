package com.barbearia.pagamentos.configuration;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OAuthFeignConfig {

    private final AsaasConfiguration asaasConfiguration;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("access_token", asaasConfiguration.getacessToken());
        };
    }
}

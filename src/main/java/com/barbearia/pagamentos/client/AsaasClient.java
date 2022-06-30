package com.barbearia.pagamentos.client;

import com.barbearia.pagamentos.configuration.OAuthFeignConfig;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        url = "https://sandbox.asaas.com/api/v3",
        name = "asaas",
        configuration = OAuthFeignConfig.class)
public interface AsaasClient {

    @PostMapping("customers")
    @Headers("Content-Type: application/json")
    void novoCliente(@RequestBody ClienteDTO cliente);

    @GetMapping("payments/{id}")
    void getCobranca(@PathVariable String id);
}

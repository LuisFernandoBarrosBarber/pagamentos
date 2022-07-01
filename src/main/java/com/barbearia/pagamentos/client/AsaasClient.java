package com.barbearia.pagamentos.client;

import com.barbearia.pagamentos.configuration.OAuthFeignConfig;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.model.asaas.AsaasCobranca;
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
    AsaasCliente novoCliente(@RequestBody ClienteDTO cliente);

    @GetMapping("payments/{id}")
    AsaasCobranca getCobranca(@PathVariable("id") String id);
}

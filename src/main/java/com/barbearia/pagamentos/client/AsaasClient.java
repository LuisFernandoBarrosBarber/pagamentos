package com.barbearia.pagamentos.client;

import com.barbearia.pagamentos.configuration.OAuthFeignConfig;
import com.barbearia.pagamentos.dto.asaas.AssinaturaDTO;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.model.asaas.AsaasAssinatura;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancas;
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

    @PostMapping("subscriptions")
    @Headers("Content-Type: application/json")
    AsaasAssinatura novaAssinatura(@RequestBody AssinaturaDTO assinatura);

//    @GetMapping("payments/{id}")
//    AsaasCobranca getCobranca(@PathVariable("id") String id);

    @GetMapping("subscriptions/{id}/payments")
    AsaasCobrancas getCobrancas(@PathVariable("id") String id);
}

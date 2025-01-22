package com.barbearia.pagamentos.client;

import com.barbearia.pagamentos.configuration.OAuthFeignConfig;
import com.barbearia.pagamentos.dto.asaas.AssinaturaDTO;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.dto.asaas.CobrancaDTO;
import com.barbearia.pagamentos.dto.asaas.ConfigNotaFiscalAssinaturaDTO;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.*;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        url = "${asaas.url}",
        name = "asaas",
        configuration = OAuthFeignConfig.class)
public interface AsaasClient {

    @PostMapping("customers")
    @Headers("Content-Type: application/json")
    AsaasCliente novoCliente(@RequestBody ClienteDTO cliente);

    @PostMapping("customers/{id}")
    AsaasCliente atualizarCliente(@PathVariable("id") String id,
                                  @RequestBody ClienteDTO cliente);

    @GetMapping("customers/{id}")
    AsaasCliente getCliente(@PathVariable("id") String id);

    @DeleteMapping("customers/{id}")
    AsaasCliente deleteCliente(@PathVariable("id") String id);

    @PostMapping("subscriptions")
    @Headers("Content-Type: application/json")
    AsaasAssinatura novaAssinatura(@RequestBody AssinaturaDTO assinatura);

    @PostMapping("subscriptions/{id}")
    @Headers("Content-Type: application/json")
    AsaasAssinatura updateAssinatura(@PathVariable("id") String id,
                                     @RequestBody AssinaturaDTO assinatura);

    @PostMapping("payments/{id}")
    @Headers("Content-Type: application/json")
    AsaasCobrancaData updateCobranca(@PathVariable("id") String id, @RequestBody CobrancaDTO c);

    @GetMapping("payments/{id}")
    AsaasCobrancaData getCobranca(@PathVariable("id") String id);

    @GetMapping("subscriptions/{id}/payments")
    AsaasCobrancas getCobrancas(@PathVariable("id") String id);

    @GetMapping("subscriptions/{id}")
    AsaasAssinatura getAssinatura(@PathVariable("id") String id);

    @PostMapping("subscriptions/{id}/invoiceSettings")
    void setNewConfigNotaFiscal(@PathVariable("id") String id,
                                           @RequestBody ConfigNotaFiscalAssinaturaDTO nfConfig);
}

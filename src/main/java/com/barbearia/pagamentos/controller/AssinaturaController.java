package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancas;
import com.barbearia.pagamentos.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("assinatura")
@RequiredArgsConstructor
public class AssinaturaController {

    private final AssinaturaService service;

    @GetMapping("{id}/cobrancas")
    public AsaasCobrancas getCobrancas(@PathVariable("id") String idAssinatura) {
        return service.getCobrancas(idAssinatura);
    }

    @GetMapping("get-by-cliente/{id}")
    public Assinatura getAssinatura(@PathVariable("id") Long idCliente) {
        return service.getByCliente(idCliente);
    }
}

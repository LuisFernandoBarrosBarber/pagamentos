package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Contrato;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancas;
import com.barbearia.pagamentos.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("assinatura")
@RequiredArgsConstructor
public class AssinaturaController {

    private final AssinaturaService service;

    @PostMapping
    public Assinatura nova(@RequestBody Contrato co) {
        return service.assinar(co);
    }
}

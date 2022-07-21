package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping("{id}/cobrancas")
    public List<Cobranca> getCobrancas(@PathVariable("id") Long id) {
        return service.getCobrancasByCliente(id);
    }

    @PostMapping("{id}/update-assinatura")
    public Assinatura updateAssinatura(@PathVariable("id") Long id,
            @RequestParam("value") float value) {
        return service.updateAssinatura(id, value);
    }
}

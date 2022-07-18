package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

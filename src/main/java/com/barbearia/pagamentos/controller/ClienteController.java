package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping(value = "novo")
    public Cliente novo() {
        return service.novo("Teste", 12345L);
    }
}

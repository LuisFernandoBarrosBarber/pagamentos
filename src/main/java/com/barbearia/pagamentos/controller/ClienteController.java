package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping(value = "novo")
    public Cliente novo(@RequestParam("nome") String nome,
            @RequestParam("id") Long id,
            @RequestParam("cpf") String cpf) {
        return service.novo(nome, id, cpf);
    }
}

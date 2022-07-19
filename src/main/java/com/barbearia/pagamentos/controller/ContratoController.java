package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.model.Contrato;
import com.barbearia.pagamentos.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("contrato")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService service;

    @PostMapping(value = "novo")
    public Contrato novo(@RequestBody @Valid Contrato c) {
        return service.novo(c);
    }
}

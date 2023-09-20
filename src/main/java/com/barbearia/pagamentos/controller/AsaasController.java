package com.barbearia.pagamentos.controller;


import com.barbearia.pagamentos.model.asaasV2.AsaasCobranca;
import com.barbearia.pagamentos.service.AsaasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("asaas")
@RequiredArgsConstructor
public class AsaasController {

    private final AsaasService service;

    @PostMapping
    public ResponseEntity newEvent(@RequestBody AsaasCobranca c) {
        return service.newEvent(c);
    }

}

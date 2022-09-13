package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
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

    @GetMapping("{id}/last-cobranca-paga")
    public Cobranca getLastCobrancaPaga(@PathVariable("id") Long id) {
        return service.getLastCobrancaPaga(id);
    }

    @PostMapping("{id}/update-assinatura")
    public Assinatura updateAssinatura(@PathVariable("id") Long id,
                                       @RequestParam("value") float value,
                                       @RequestParam("forma-pagamento") BillingTypeEnum fp) {
        return service.updateAssinatura(id, value, fp);
    }

    @PostMapping
    public Cliente novo(@RequestParam("id") Long id, @RequestParam("nome") String nome,
                        @RequestParam("cpf") String cpf) {
        return service.novo(nome, id, cpf);
    }

    @DeleteMapping("{id}/cancelar-assinatura")
    public Assinatura cancelarAssinatura(@PathVariable("id") Long id) {
        return service.cancelarAssinatura(id);
    }

    @GetMapping("{id}/assinatura")
    public Assinatura getAssinatura(@PathVariable("id") Long id) {
        return service.getAssinatura(id);
    }

}

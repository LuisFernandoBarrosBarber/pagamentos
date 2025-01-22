package com.barbearia.pagamentos.controller;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

//    @GetMapping("{id}/cobrancas")
//    public List<Cobranca> getCobrancas(@PathVariable("id") Long id) {
//        return service.getCobrancasByCliente(id);
//    }

    @GetMapping("lote/cobrancas")
    public List<Cobranca> getCobrancas(@RequestParam("ids") List<Long> ids) {
        return service.getCobrancasByClientes(ids);
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
    public Cliente novo(@RequestParam(value = "cep", required = false) String cep,
                        @RequestParam(value = "rua", required = false) String rua,
                        @RequestParam(value = "bairro", required = false) String bairro,
                        @RequestParam(value = "cidade", required = false) String cidade,
                        @RequestParam(value = "numero", required = false) String numero,
                        @RequestParam(value = "email") String email,
                        @RequestParam(value = "telefone") String telefone,
                        @RequestParam("id") Long id,
                        @RequestParam("nome") String nome,
                        @RequestParam("cpf") String cpf,
                        @RequestParam("barbearia-nome") String barbeariaNome) {
        return service.novo(nome, id, cpf, barbeariaNome, cep, rua, bairro, cidade, numero, email, telefone);
    }

    @GetMapping("/assinatura-by-profissionais")
    public Assinatura getAssinatura(@RequestParam("ids") List<Long> ids) {
        return service.getAssinatura(ids);
    }

    @GetMapping("{data}")
    public List<Cliente> getVenceEm(@PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return service.getVenceEm(data);
    }

}

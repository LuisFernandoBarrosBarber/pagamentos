package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.Contrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ClienteService clienteService;
    private final AssinaturaService assinaturaService;

    @Transactional
    public Contrato novo(Contrato co) {
        Cliente cl = clienteService.novo(co.getNome(), co.getId(), co.getCpf());
        Assinatura a = assinaturaService.assinar(cl, co);
        co.setAssinaturaIdAsaas(a.getIdAsaas());
        co.setClienteIdAsaas(cl.getIdAsaas());
        return co;
    }
}

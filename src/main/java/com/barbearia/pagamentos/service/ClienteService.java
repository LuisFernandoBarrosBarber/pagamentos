package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.converter.ClienteEntityToCliente;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.time.LocalDateTime.now;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final AsaasClient asaasClient;
    private final ClienteRepository repo;
    private final ClienteEntityToCliente toCliente;

    @Transactional
    public Cliente novo(String nome, Long id) {
        ClienteEntity c = salvamentoInicial(id);
        try {
            AsaasCliente asaasCliente = asaasClient.novoCliente(
                    ClienteDTO.builder()
                            .name(nome)
                            .externalReference(id.toString())
                            .build());
            c.setIdAsaas(asaasCliente.getId());

        } catch (Exception e) {
            log.error("ERRO AO SALVAR CLIENTE. CLIENTE ID: " + id, e);
            throw e;
        }
        //asaasClient.getCobranca("01176394");

        return toCliente.apply(c);
    }

    @Transactional(REQUIRES_NEW)
    public ClienteEntity salvamentoInicial(Long id) {
        ClienteEntity c = new ClienteEntity();
        c.setAtivo(true);
        c.setCriadoEm(now());
        c.setId(id);
        return repo.save(c);
    }

}

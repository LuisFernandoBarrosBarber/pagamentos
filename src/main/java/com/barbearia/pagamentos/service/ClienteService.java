package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.configuration.excetion.ResourceNotFoundException;
import com.barbearia.pagamentos.converter.ClienteEntityToCliente;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final AsaasClient asaasClient;
    private final ClienteRepository repo;
    private final ClienteEntityToCliente toCliente;
    private final CobrancaService cobrancaService;
    private final AssinaturaService assinaturaService;

    @Transactional
    public Cliente novo(String nome, Long id, String cpf) {
        testAlreadyAssinado(id);
        try {
            AsaasCliente c = asaasClient.novoCliente(getDTO(nome, id, cpf));
            ClienteEntity entity = save(id, c.getId());
            return toCliente.apply(entity);
        } catch (Exception e) {
            log.error("ERRO AO SALVAR CLIENTE. CLIENTE ID: " + id, e);
            throw e;
        }
    }

    @Transactional
    public Assinatura updateAssinatura(Long id, float value) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return assinaturaService.updateAssinatura(a.getIdAsaas(), value);
    }

    @Transactional
    public Assinatura cancelarAssinatura(Long id) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return assinaturaService.cancelarAssinatura(a.getIdAsaas());
    }

    public List<Cobranca> getCobrancasByCliente(Long id) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return cobrancaService.getByAssinatura(a.getIdAsaas());
    }

    public Cobranca getLastCobrancaPaga(Long id) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return cobrancaService.getLastCobrancaPagaByAssinatura(a.getIdAsaas());
    }

    public ClienteEntity save(Long id, String idAssas) {
        ClienteEntity c = new ClienteEntity();
        c.setAtivo(true);
        c.setCriadoEm(now());
        c.setId(id);
        c.setIdAsaas(idAssas);
        return repo.save(c);
    }

    private void testAlreadyAssinado(Long id) {
        if (repo.countAllByIdAndAtivoIsTrueAndIdAsaasNotNull(id) > 0) {
            throw new RuntimeException("Cliente já cadastrado");
        }
    }

    private void testClienteExists(Long id) {
        if (repo.findByIdAndAtivoIsTrue(id).isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado ou inativado.");
        }
    }

    private static ClienteDTO getDTO(String nome, Long id, String cpf) {
        return ClienteDTO.builder()
                .name(nome)
                .externalReference(id.toString())
                .cpfCnpj(cpf)
                .build();
    }
}

package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.configuration.excetion.ResourceNotFoundException;
import com.barbearia.pagamentos.dto.asaas.AssinaturaDTO;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.Contrato;
import com.barbearia.pagamentos.model.asaas.AsaasAssinatura;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancas;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import static com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum.UNDEFINED;
import static java.time.LocalDateTime.now;

@Service
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository repo;
    private final AsaasClient asaasClient;
    private final CobrancaService cobrancaService;

    @Transactional
    public Assinatura assinar(Cliente c, Contrato co) {

        check(c);
        AsaasAssinatura assinatura;
        try {
            assinatura = asaasClient.novaAssinatura(getDTO(c, co));
            salvaCobrancas(assinatura);
            save(assinatura, c);
        } catch (Exception e) {
            log.error("ERRO AO GERAR ASSINATURA/COBRANCA DO CLIENTE " + c.getId(), e);
            throw e;
        }

        return Assinatura.builder().idAsaas(assinatura.getId()).build();

    }

    @Transactional
    public void salvaCobrancas(AsaasAssinatura a) {
        getCobrancasByAssinatura(a.getId())
                .getData()
                .forEach(cobrancaService::nova);
    }

    public AsaasCobrancas getCobrancasByAssinatura(String assinaturaId) {
        return asaasClient.getCobrancas(assinaturaId);
    }

    private static AssinaturaDTO getDTO(Cliente c, Contrato co) {
        return AssinaturaDTO.builder()
                .billingType(UNDEFINED)
                .value(co.getValor())
                .customer(c.getIdAsaas())
                .cycle(co.getCiclo())
                .description("Assinatura do plano Barbeiro Agenda.")
                .nextDueDate(LocalDate.now().plusDays(30))
                .build();
    }

    @Transactional
    public Assinatura getByCliente(Long id) {

        AssinaturaEntity a = repo.getByIdClienteAndAtivoIsTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada."));

        return Assinatura.builder().idAsaas(a.getIdAssinatura()).build();
    }

    private void save(AsaasAssinatura a, Cliente c) {
        AssinaturaEntity entity = new AssinaturaEntity();
        entity.setIdAssinatura(a.getId());
        entity.setIdCliente(c.getId());
        entity.setAtivo(true);
        entity.setCriadoEm(now());
        repo.save(entity);
    }

    private void check(Cliente c) {
        if (repo.countAllByAtivoIsTrueAndIdCliente(c.getId()) > 0) {
            throw new RuntimeException("Cliente já possuí assinatura");
        }
    }

}

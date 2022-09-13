package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.configuration.excetion.ResourceNotFoundException;
import com.barbearia.pagamentos.dto.asaas.AssinaturaDTO;
import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.model.Assinatura;
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
    public Assinatura assinar(Contrato co) {

        check(co.getId());
        AsaasAssinatura assinatura;
        try {
            assinatura = asaasClient.novaAssinatura(getDTO(co));
            salvaCobrancas(assinatura);
            save(assinatura, co);
        } catch (Exception e) {
            log.error("ERRO AO GERAR ASSINATURA/COBRANCA DO CLIENTE " + co.getId(), e);
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

    @Transactional
    public Assinatura updateAssinatura(String id, Float value, BillingTypeEnum fp) {

        AssinaturaEntity e = getEntityById(id);

        AssinaturaDTO aDTO = AssinaturaDTO.builder()
                .value(value)
                .billingType(fp)
                .build();

        Assinatura aa = Assinatura.builder()
                .idAsaas(asaasClient.updateAssinatura(id, aDTO).getId())
                .build();

        e.setFormaPagamento(fp);
        cobrancaService.updateByAssinaturaId(id, fp);
        return aa;
    }

    @Transactional
    public Assinatura cancelarAssinatura(String id) {
        AssinaturaEntity e = getEntityById(id);
        e.setAtivo(false);

        return Assinatura.builder()
                .idAsaas(asaasClient.cancelarAssinatura(id).getId())
                .build();
    }

    public AsaasCobrancas getCobrancasByAssinatura(String assinaturaId) {
        return asaasClient.getCobrancas(assinaturaId);
    }

    private static AssinaturaDTO getDTO(Contrato co) {
        return AssinaturaDTO.builder()
                .billingType(co.getFormaPagamento())
                .value(co.getValor())
                .customer(co.getClienteIdAsaas())
                .cycle(co.getCiclo())
                .description("Assinatura do plano Barbeiro Agenda.")
                .nextDueDate(LocalDate.now())
                .build();
    }

    @Transactional
    public Assinatura getByCliente(Long id) {

        AssinaturaEntity a = repo.getByIdClienteAndAtivoIsTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada ou finalizada."));

        return Assinatura.builder()
                .idAsaas(a.getIdAssinatura())
                .formaPagamento(a.getFormaPagamento())
                .build();
    }


    private AssinaturaEntity getEntityById(String id) {
        return repo.findByAtivoIsTrueAndIdAssinatura(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada ou finalizada."));
    }

    private void save(AsaasAssinatura a, Contrato c) {
        AssinaturaEntity entity = new AssinaturaEntity();
        entity.setIdAssinatura(a.getId());
        entity.setIdCliente(c.getId());
        entity.setAtivo(true);
        entity.setCriadoEm(now());
        entity.setFormaPagamento(c.getFormaPagamento());
        repo.save(entity);
    }

    private void check(Long idCliente) {
        if (repo.countAllByAtivoIsTrueAndIdCliente(idCliente) > 0) {
            throw new RuntimeException("Cliente já possuí assinatura");
        }
    }

}

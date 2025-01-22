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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository repo;
    private final AsaasClient asaasClient;
    private final CobrancaService cobrancaService;
    private final NotaFiscalService notaFiscalService;

    @Transactional
    public Assinatura assinar(Contrato co) {

        check(co.getId());
        AsaasAssinatura assinatura;
        try {
            assinatura = asaasClient.novaAssinatura(getDTO(co));
            salvaCobrancas(assinatura);
            save(assinatura, co);
            // DESABILITADO AQUI. HABILITAR QUANDO FOR PRA EMITIR NOTA FISCAL MESMO.
            // notaFiscalService.newConfigNF(assinatura.getId());
        } catch (Exception e) {
            log.error("ERRO AO GERAR ASSINATURA/COBRANCA DO CLIENTE " + co.getId(), e);
            throw e;
        }

        return Assinatura.builder().id(assinatura.getId()).build();

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
                .id(asaasClient.updateAssinatura(id, aDTO).getId())
                .build();

        e.setFormaPagamento(fp);
        cobrancaService.updateByAssinaturaId(id, fp, value);
        return aa;
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
                .nextDueDate(co.getDtFirstFatura())
                .build();
    }

    @Transactional
    public Assinatura getByCliente(Long id) {

        AssinaturaEntity a = repo.getByIdClienteAndAtivoIsTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada ou finalizada."));

        return Assinatura.builder()
                .id(a.getIdAssinatura())
                .formaPagamento(a.getFormaPagamento())
                .build();
    }

    @Transactional
    public Assinatura getByAllProfissionaisDaBarbearia(List<Long> idsP) {
        List<Assinatura> assinaturas = new ArrayList<>();
        idsP.forEach(id -> {
            repo.getByIdClienteAndAtivoIsTrue(id).ifPresent(a -> {
                assinaturas.add(Assinatura.builder()
                        .id(a.getIdAssinatura())
                        .formaPagamento(a.getFormaPagamento())
                        .build());
            });
        });

        // CASO NÃO ENCONTRE NENHUMA ASSINATURA ATIVA PARA TODOS OS PROFISSIONAIS DA BARBEARIA
        if (assinaturas.isEmpty()) {
            throw new ResourceNotFoundException("Assinatura não encontrada ou finalizada.");
            // CASO A BARBEARIA TENHA MAIS DE UMA ASSINATURA ATIVA
        } else if (assinaturas.size() > 1) {
            throw new RuntimeException("Essa barbearia possui mais de uma assinatura ativa. Contate o suporte!");
        }
        // APENAS UMA ASSINATURA ATIVA, TUDO OK!
        return assinaturas.get(0);
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

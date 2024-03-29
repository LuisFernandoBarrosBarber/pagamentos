package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.converter.AsaasCobrancaDataToCobrancaEntity;
import com.barbearia.pagamentos.converter.CobrancaEntityToCobranca;
import com.barbearia.pagamentos.dto.asaas.CobrancaDTO;
import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca.*;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class CobrancaService {

    private final CobrancaRepository repo;
    private final AsaasClient client;
    private final AsaasCobrancaDataToCobrancaEntity asaasToEntity;
    private final CobrancaEntityToCobranca toCobranca;

    @Transactional
    public void nova(AsaasCobrancaData c) {
        if (!isCobrancaDuplicada(c)) {
            repo.save(asaasToEntity.apply(c));
        } else {
            log.info("COBRANCA DUPLICADA IGNORADA", c);
        }
    }

    @Transactional
    public void updateByAssinaturaId(String aId, BillingTypeEnum fp, float value) {
        repo.getByIdAssinaturaAndAtivoIsTrueAndStatusIn(aId, Arrays.asList(PENDING, OVERDUE))
                .forEach(it -> {
                    CobrancaDTO cDTO = CobrancaDTO.builder()
                            .billingType(fp)
                            .value(value)
                            .build();
                    try {
                        client.updateCobranca(it.getIdCobranca(), cDTO);
                        it.setTipoPagamento(fp);
                        it.setValor(value);
                        repo.save(it);
                    } catch (Exception e) {
                        log.info("ERRO AO ALTERAR COBRANCA. ", e);
                    }
                });

    }

    @Transactional
    public void update(Cobranca c, AsaasCobrancaData ca) {
        repo.findById(c.id)
                .ifPresent(ce -> {
                    ce.setStatus(ca.getStatus());
                    ce.setPagamentoEm(getDataPagamento(ca));
                    ce.setTipoPagamento(ca.getBillingType());
                    ce.setAtivo(!ca.isDeleted());
                    ce.setLastUpdate(now());
                });
    }

    @Transactional
    public List<Cobranca> getByAssinatura(String assinaturaId) {

        return repo.getByIdAssinaturaAndAtivoIsTrue(assinaturaId)
                .map(toCobranca)
                .collect(Collectors.toList());
    }

    @Transactional
    public Cobranca getLastCobrancaPagaByAssinatura(String idAsaas) {
        return repo.findFirstByAtivoIsTrueAndIdAssinaturaAndStatusInOrderByVencimentoEmDesc(idAsaas,
                        Arrays.asList(CONFIRMED, RECEIVED, RECEIVED_IN_CASH))
                .map(toCobranca)
                .orElse(Cobranca.builder().build());

    }


    @Transactional
    public Optional<Cobranca> findByIdAsaas(String id) {
        return repo.findByIdCobranca(id)
                .stream().map(toCobranca)
                .findFirst();
    }

    public boolean isOnlyUmaCobrancaPendente(List<Cobranca> cs) {
        return cs
                .stream()
                .filter(c -> c.getStatus().equals(PENDING))
                .count() == 1;
    }

    private LocalDateTime getDataPagamento(AsaasCobrancaData c) {
        return c.getPaymentDate() != null ?
                c.getPaymentDate().atTime(LocalTime.from(now())) :
                null;
    }

    // AS VEZES ASAAS RECEBE VALORES DUPLICADOS, AI ELA GERA UMA NOVA COBRANCA
    // SEM ASSINATURA. DA PROBLEMA NO SISTEMA.
    // POR ENQUANTO, VAMOS IGNORAR ESTA COBRANCA
    // SE OCORRER MUITO, VER UMA SOLUÇÃO MELHOR.
    private boolean isCobrancaDuplicada(AsaasCobrancaData c) {
        return c.getSubscription() == null;
    }
}

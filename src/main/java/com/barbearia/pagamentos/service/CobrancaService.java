package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import static com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca.RECEIVED;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final CobrancaRepository repo;

    @Transactional
    public void nova(AsaasCobrancaData c) {
        repo.save(getEntity(c));
    }

    @Transactional
    public List<Cobranca> getByAssinatura(String assinaturaId) {

        return repo.getByIdAssinaturaAndAtivoIsTrue(assinaturaId)
                .map(this::toCobranca)
                .collect(Collectors.toList());
    }

    @Transactional
    public Cobranca getLastCobrancaPagaByAssinatura(String idAsaas) {
        return repo.findFirstByAtivoIsTrueAndIdAssinaturaAndStatusOrderByVencimentoEm(idAsaas, RECEIVED)
                .map(this::toCobranca)
                .orElse(Cobranca.builder().build());

    }

    private CobrancaEntity getEntity(AsaasCobrancaData ca) {
        CobrancaEntity c = new CobrancaEntity();
        c.setIdCobranca(ca.getId());
        c.setAtivo(true);
        c.setCriadoEm(now());
        c.setIdAssinatura(ca.getSubscription());
        c.setVencimentoEm(ca.getDueDate());
        c.setStatus(ca.getStatus());
        c.setInvoiceUrl(ca.getInvoiceUrl());
        c.setValor(ca.getValue());
        c.setTipoPagamento(ca.getBillingType());
        return c;
    }

    private Cobranca toCobranca(CobrancaEntity c) {
        return Cobranca.builder()
                .id(c.getId())
                .idCobranca(c.getIdCobranca())
                .idAssinatura(c.getIdAssinatura())
                .ativo(c.isAtivo())
                .criadoEm(c.getCriadoEm())
                .pagamentoEm(c.getPagamentoEm())
                .status(c.getStatus())
                .vencimentoEm(c.getVencimentoEm())
                .valor(c.getValor())
                .invoiceUrl(c.getInvoiceUrl())
                .tipoPagamento(c.getTipoPagamento())
                .build();
    }
}

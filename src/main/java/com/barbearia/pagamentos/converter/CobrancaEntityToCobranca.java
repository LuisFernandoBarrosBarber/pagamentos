package com.barbearia.pagamentos.converter;

import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CobrancaEntityToCobranca implements Function<CobrancaEntity, Cobranca> {
    @Override
    public Cobranca apply(CobrancaEntity c) {
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

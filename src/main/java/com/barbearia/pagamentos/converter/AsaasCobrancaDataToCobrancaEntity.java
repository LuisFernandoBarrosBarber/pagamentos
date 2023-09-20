package com.barbearia.pagamentos.converter;

import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static java.time.LocalDateTime.now;

@Service
public class AsaasCobrancaDataToCobrancaEntity  implements Function<AsaasCobrancaData, CobrancaEntity> {
    @Override
    public CobrancaEntity apply(AsaasCobrancaData ca) {
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
}

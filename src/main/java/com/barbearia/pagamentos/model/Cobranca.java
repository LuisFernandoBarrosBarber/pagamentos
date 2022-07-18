package com.barbearia.pagamentos.model;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class Cobranca {
    public Long id;
    public String idAssinatura;
    public String idCobranca;
    public boolean ativo;
    public LocalDateTime criadoEm;
    public LocalDate vencimentoEm;
    public StatusCobranca status;
    public LocalDateTime pagamentoEm;
    public BillingTypeEnum tipoPagamento;
    public String invoiceUrl;
    public float valor;
}

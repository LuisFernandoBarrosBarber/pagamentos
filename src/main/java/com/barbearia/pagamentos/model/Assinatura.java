package com.barbearia.pagamentos.model;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Assinatura {
    String idAsaas;
    BillingTypeEnum formaPagamento;
}

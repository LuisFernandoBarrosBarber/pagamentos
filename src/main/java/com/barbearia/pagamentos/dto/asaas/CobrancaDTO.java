package com.barbearia.pagamentos.dto.asaas;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CobrancaDTO {
    BillingTypeEnum billingType;
}

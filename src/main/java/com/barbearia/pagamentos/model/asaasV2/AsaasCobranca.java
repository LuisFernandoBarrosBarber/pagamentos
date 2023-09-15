package com.barbearia.pagamentos.model.asaasV2;

import com.barbearia.pagamentos.dto.asaas.enumerator.EventoCobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AsaasCobranca {
    EventoCobranca event;
    AsaasCobrancaData payment;
}

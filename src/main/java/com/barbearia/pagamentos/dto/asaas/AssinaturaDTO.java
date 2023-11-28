package com.barbearia.pagamentos.dto.asaas;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.dto.asaas.enumerator.CycleEnum;
import com.barbearia.pagamentos.dto.asaas.enumerator.StatusAssinatura;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class AssinaturaDTO {
    String customer; // ID DO CLIENTE
    BillingTypeEnum billingType; // FORMA DE PAGAMENTO
    LocalDate nextDueDate; // VENCIMENTO DA PRIMEIRA MENSALIDADE
    float value; // VALOR DA ASSINATURA
    CycleEnum cycle; // CICLO DE COBRANCA
    String description; // DESCRICAO DA ASSINATURA
}

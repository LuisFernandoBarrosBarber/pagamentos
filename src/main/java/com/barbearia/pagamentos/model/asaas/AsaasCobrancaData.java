package com.barbearia.pagamentos.model.asaas;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AsaasCobrancaData {
    String id;
    LocalDate dateCreated;
    String customer;
    String subscription;
    float value;
    BillingTypeEnum billingType;
    StatusCobranca status;
    LocalDate originalDueDate;
    LocalDate dueDate;
    LocalDate paymentDate;
    String invoiceUrl;
}

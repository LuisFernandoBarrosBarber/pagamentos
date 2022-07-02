package com.barbearia.pagamentos.dto.asaas.enumerator;

public enum StatusCobranca {
    PENDING,
    ECEIVED,
    CONFIRMED,
    OVERDUE,
    RECEIVED_IN_CASH,
    REFUND_REQUESTED,
    CHARGEBACK_REQUESTED,
    CHARGEBACK_DISPUTE,
    AWAITING_CHARGEBACK_REVERSAL,
    DUNNING_REQUESTED,
    DUNNING_RECEIVED,
    AWAITING_RISK_ANALYSIS
}

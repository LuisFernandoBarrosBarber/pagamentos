package com.barbearia.pagamentos.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Assinatura {
    String idAsaas;
    boolean ativo;
}

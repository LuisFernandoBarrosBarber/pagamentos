package com.barbearia.pagamentos.model.asaas;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AsaasCliente {
    String id;
    boolean deleted;
    String externalReference;
}

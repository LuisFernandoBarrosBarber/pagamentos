package com.barbearia.pagamentos.model.asaas;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AsaasCobrancas {
    int totalCount;
    boolean hasMore;
    List<AsaasCobrancaData> data;
}

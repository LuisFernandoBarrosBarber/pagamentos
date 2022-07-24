package com.barbearia.pagamentos.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Cliente {
    Long id;
    String idAsaas;
    LocalDateTime criadoEm;
    boolean ativo;
    String cpf;
}

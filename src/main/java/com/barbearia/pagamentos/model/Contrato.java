package com.barbearia.pagamentos.model;

import com.barbearia.pagamentos.dto.asaas.enumerator.CycleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
public class Contrato {

    @NotNull(message = "Identificador do cliente é obrigatório")
    private Long id;
    @NotNull(message = "Nome é obrigatório")
    private String nome;
    @NotNull(message = "CPF é obrigatório")
    String cpf;
    @NotNull(message = "Valor é obrigatório")
    float valor;
    @NotNull(message = "Ciclo de renovação é obrigatório")
    CycleEnum ciclo;
    @Null
    String clienteIdAsaas;
    @Null
    String assinaturaIdAsaas;
}

package com.barbearia.pagamentos.configuration.excetion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Erro {
    private String mensagem;
}

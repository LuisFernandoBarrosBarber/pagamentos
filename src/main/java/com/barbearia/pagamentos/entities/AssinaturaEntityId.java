package com.barbearia.pagamentos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssinaturaEntityId implements Serializable {
    private Long idCliente;
    private String idAssinatura;
}

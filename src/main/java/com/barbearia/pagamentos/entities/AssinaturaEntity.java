package com.barbearia.pagamentos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "assinatura_asaas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AssinaturaEntityId.class)
public class AssinaturaEntity implements Serializable {

    @Id
    private Long idCliente;

    @Id
    private String idAssinatura;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private LocalDateTime criadoEm;
}

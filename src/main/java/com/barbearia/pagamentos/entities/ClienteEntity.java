package com.barbearia.pagamentos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "cliente_asaas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    @Id
    private Long id;

    private String idAsaas;

    @Column(nullable = false)
    private boolean ativo;

    @Column
    private LocalDateTime criadoEm;
}

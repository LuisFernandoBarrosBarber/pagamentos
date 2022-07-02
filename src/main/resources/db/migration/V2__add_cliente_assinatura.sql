CREATE TABLE assinatura_asaas
(
    id_cliente    INT          NOT NULL,
    id_assinatura VARCHAR(255) NOT NULL,
    ativo         BOOLEAN      NOT NULL,
    criado_em     datetime     NOT NULL,
    PRIMARY KEY (id_cliente, id_assinatura)
);

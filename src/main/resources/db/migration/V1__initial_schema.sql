CREATE TABLE cliente_asaas
(
    id        INT     NOT NULL,
    id_asaas  VARCHAR(255) NULL,
    ativo     BOOLEAN NOT NULL,
    criado_em datetime NULL,
    PRIMARY KEY (id)
);

CREATE TABLE gasto (
    id UUID PRIMARY KEY,
    descricao VARCHAR(150),
    gasto_tipo VARCHAR(50) NOT NULL,
    valor NUMERIC(6,2) NOT NULL,
    data_gasto DATE,
    id_usuario UUID,

    CONSTRAINT fk_gasto_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id)
        ON DELETE CASCADE
);

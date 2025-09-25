package io.github.wendergustavo.gastospessoais.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = "usuario")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "descricao", length = (150))
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "gasto_tipo",nullable = false)
    private GastoTipo gastoTipo;

    @Column(name = "valor",precision = 6,scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_gasto")
    private LocalDate dataGasto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}

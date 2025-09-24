package io.github.wendergustavo.gastospessoais.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "description", length = (150))
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "spent_Type",nullable = false)
    private GastoTipo spentType;

    @Column(name = "value",precision = 6,scale = 2, nullable = false)
    private BigDecimal value;

    @Column(name = "spent_date")
    private LocalDate spentDate;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}

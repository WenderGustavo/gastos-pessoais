package io.github.wendergustavo.gastospessoais.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "usuario")
public class Gasto implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDate.now();
    }
}

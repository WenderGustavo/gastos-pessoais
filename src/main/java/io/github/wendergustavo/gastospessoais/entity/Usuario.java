package io.github.wendergustavo.gastospessoais.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "gastos")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length =(100),nullable = false)
    private String nome;

    @Column(name = "email",nullable = false,length =(250), unique = true)
    private String email;

    @Column(name = "senha",length =(128), nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name ="role" ,length=20,nullable = false )
    private Roles role;

    @OneToMany(mappedBy = "usuario")
    private List<Gasto> gastos;

}

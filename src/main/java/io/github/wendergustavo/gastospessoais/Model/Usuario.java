package io.github.wendergustavo.gastospessoais.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@Data
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length =(100),nullable = false)
    private String nome;

    @Column(name = "email",nullable = false,length =(250), unique = true)
    private String email;

    @Column(name = "senha",length =(250), nullable = false)
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Gasto> gastos;

}

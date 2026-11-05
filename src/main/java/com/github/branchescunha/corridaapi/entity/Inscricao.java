package com.github.branchescunha.corridaapi.entity;

import com.github.branchescunha.corridaapi.enums.Percurso;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Percurso percurso;

    public Inscricao() {
    }

    public Inscricao(
            String nome,
            String email,
            LocalDate dataNascimento,
            Percurso percurso
    ) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.percurso = percurso;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Percurso getPercurso() {
        return percurso;
    }

    public void setPercurso(Percurso percurso) {
        this.percurso = percurso;
    }
}
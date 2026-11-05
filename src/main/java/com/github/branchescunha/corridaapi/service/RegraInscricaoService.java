package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.enums.Percurso;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class RegraInscricaoService {

    private static final LocalDate DATA_PROVA = LocalDate.of(2027, 3, 14);
    private static final int IDADE_MINIMA = 14;

    public int calcularIdadeNaProva(LocalDate dataNascimento) {
        return Period.between(dataNascimento, DATA_PROVA).getYears();
    }

    public Categoria calcularCategoria(LocalDate dataNascimento) {
        int idade = calcularIdadeNaProva(dataNascimento);

        if (idade <= 17) {
            return Categoria.JUVENIL;
        }

        if (idade <= 39) {
            return Categoria.ADULTO;
        }

        if (idade <= 59) {
            return Categoria.MASTER;
        }

        return Categoria.SENIOR;
    }

    public void validarParticipacao(LocalDate dataNascimento, Percurso percurso) {
        int idade = calcularIdadeNaProva(dataNascimento);

        if (idade < IDADE_MINIMA) {
            throw new IllegalArgumentException(
                    "A idade mínima para participar é 14 anos."
            );
        }

        if (percurso == Percurso.KM_10 && idade < 18) {
            throw new IllegalArgumentException(
                    "O percurso de 10 km é permitido apenas para participantes com 18 anos ou mais."
            );
        }
    }

    public String normalizarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }

    public String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }

    public LocalDate getDataProva() {
        return DATA_PROVA;
    }
}
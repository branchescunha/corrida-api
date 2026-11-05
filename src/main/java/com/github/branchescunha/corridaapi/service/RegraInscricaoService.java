package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.enums.Percurso;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class RegraInscricaoService {

    private static final LocalDate DATA_PROVA = LocalDate.of(2027, 3, 14);

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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

    public void validarParticipacao(
            LocalDate dataNascimento,
            Percurso percurso
    ) {
        int idade = calcularIdadeNaProva(dataNascimento);

        if (idade < 14) {
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
        if (nome == null) {
            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }

        String nomeNormalizado = nome
                .trim()
                .replaceAll("\\s+", " ");

        if (nomeNormalizado.length() < 3
                || nomeNormalizado.length() > 100) {
            throw new IllegalArgumentException(
                    "O nome deve ter entre 3 e 100 caracteres."
            );
        }

        return nomeNormalizado;
    }

    public String normalizarEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException(
                    "O e-mail é obrigatório."
            );
        }

        String emailNormalizado = email
                .trim()
                .toLowerCase(Locale.ROOT);

        if (!EMAIL_PATTERN.matcher(emailNormalizado).matches()) {
            throw new IllegalArgumentException(
                    "O e-mail deve ser válido."
            );
        }

        return emailNormalizado;
    }

    public LocalDate getDataProva() {
        return DATA_PROVA;
    }
}
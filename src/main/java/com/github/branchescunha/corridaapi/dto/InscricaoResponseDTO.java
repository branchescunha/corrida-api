package com.github.branchescunha.corridaapi.dto;

import com.github.branchescunha.corridaapi.enums.Percurso;

import java.time.LocalDate;

public record InscricaoResponseDTO(
        String nome,
        String email,
        LocalDate dataNascimento,
        Percurso percurso,
        int idadeNaProva,
        String categoria
) {
}
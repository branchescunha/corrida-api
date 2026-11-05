package com.github.branchescunha.corridaapi.dto;

import com.github.branchescunha.corridaapi.enums.Percurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InscricaoEdicaoDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser anterior a hoje.")
        LocalDate dataNascimento,

        @NotNull(message = "O percurso é obrigatório.")
        Percurso percurso

) {
}
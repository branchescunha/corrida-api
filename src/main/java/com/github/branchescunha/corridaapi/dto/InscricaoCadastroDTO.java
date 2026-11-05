package com.github.branchescunha.corridaapi.dto;

import com.github.branchescunha.corridaapi.enums.Percurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record InscricaoCadastroDTO(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        String email,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser anterior a hoje.")
        LocalDate dataNascimento,

        @NotNull(message = "O percurso é obrigatório.")
        Percurso percurso

) {
}
package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.enums.Percurso;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegraInscricaoServiceTest {

    private final RegraInscricaoService service = new RegraInscricaoService();

    @Test
    void deveCalcularIdadeNaDataDaProva() {
        LocalDate nascimento = LocalDate.of(2000, 5, 10);

        int idade = service.calcularIdadeNaProva(nascimento);

        assertEquals(26, idade);
    }

    @Test
    void deveClassificarParticipanteJuvenil() {
        LocalDate nascimento = LocalDate.of(2010, 1, 1);

        Categoria categoria = service.calcularCategoria(nascimento);

        assertEquals(Categoria.JUVENIL, categoria);
    }

    @Test
    void deveClassificarParticipanteAdulto() {
        LocalDate nascimento = LocalDate.of(2000, 1, 1);

        Categoria categoria = service.calcularCategoria(nascimento);

        assertEquals(Categoria.ADULTO, categoria);
    }

    @Test
    void deveClassificarParticipanteMaster() {
        LocalDate nascimento = LocalDate.of(1980, 1, 1);

        Categoria categoria = service.calcularCategoria(nascimento);

        assertEquals(Categoria.MASTER, categoria);
    }

    @Test
    void deveClassificarParticipanteSenior() {
        LocalDate nascimento = LocalDate.of(1960, 1, 1);

        Categoria categoria = service.calcularCategoria(nascimento);

        assertEquals(Categoria.SENIOR, categoria);
    }

    @Test
    void deveRecusarParticipanteComMenosDe14Anos() {
        LocalDate nascimento = LocalDate.of(2015, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validarParticipacao(nascimento, Percurso.KM_5)
        );
    }

    @Test
    void deveRecusar10KmParaMenorDe18Anos() {
        LocalDate nascimento = LocalDate.of(2010, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validarParticipacao(nascimento, Percurso.KM_10)
        );
    }

    @Test
    void devePermitir5KmParaParticipanteCom14Anos() {
        LocalDate nascimento = LocalDate.of(2013, 3, 14);

        assertDoesNotThrow(
                () -> service.validarParticipacao(nascimento, Percurso.KM_5)
        );
    }

    @Test
    void deveNormalizarEmail() {
        String email = service.normalizarEmail("  ANDRE@EXAMPLE.COM  ");

        assertEquals("andre@example.com", email);
    }

    @Test
    void deveNormalizarEspacosDoNome() {
        String nome = service.normalizarNome("  André    Vinícius  ");

        assertEquals("André Vinícius", nome);
    }
}
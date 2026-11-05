package com.github.branchescunha.corridaapi.controller;

import com.github.branchescunha.corridaapi.dto.ResumoDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoCadastroDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoEdicaoDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoResponseDTO;
import com.github.branchescunha.corridaapi.service.InscricaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService service;

    public InscricaoController(InscricaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InscricaoResponseDTO> cadastrar(
            @Valid @RequestBody InscricaoCadastroDTO dados
    ) {
        InscricaoResponseDTO inscricao = service.cadastrar(dados);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inscricao);
    }

    @GetMapping
    public ResponseEntity<List<InscricaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoDTO> obterResumo() {
        return ResponseEntity.ok(service.obterResumo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<InscricaoResponseDTO> buscarPorEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @PutMapping("/{email}")
    public ResponseEntity<InscricaoResponseDTO> atualizar(
            @PathVariable String email,
            @Valid @RequestBody InscricaoEdicaoDTO dados
    ) {
        return ResponseEntity.ok(service.atualizar(email, dados));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> cancelar(
            @PathVariable String email
    ) {
        service.cancelar(email);

        return ResponseEntity.noContent().build();
    }
}
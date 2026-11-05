package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.dto.ResumoDTO;
import com.github.branchescunha.corridaapi.enums.Percurso;
import com.github.branchescunha.corridaapi.dto.InscricaoCadastroDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoEdicaoDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoResponseDTO;
import com.github.branchescunha.corridaapi.entity.Inscricao;
import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.repository.InscricaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InscricaoService {

    private final InscricaoRepository repository;
    private final RegraInscricaoService regras;

    public InscricaoService(
            InscricaoRepository repository,
            RegraInscricaoService regras
    ) {
        this.repository = repository;
        this.regras = regras;
    }

    @Transactional
    public InscricaoResponseDTO cadastrar(InscricaoCadastroDTO dados) {
        String nome = regras.normalizarNome(dados.nome());
        String email = regras.normalizarEmail(dados.email());

        if (repository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Já existe uma inscrição ativa para este e-mail."
            );
        }

        regras.validarParticipacao(
                dados.dataNascimento(),
                dados.percurso()
        );

        Inscricao inscricao = new Inscricao(
                nome,
                email,
                dados.dataNascimento(),
                dados.percurso()
        );

        return converterParaResponse(repository.save(inscricao));
    }

    @Transactional(readOnly = true)
    public List<InscricaoResponseDTO> listar() {
        return repository.findAllByOrderByIdAsc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InscricaoResponseDTO buscarPorEmail(String email) {
        return converterParaResponse(buscarEntidadePorEmail(email));
    }

    @Transactional
    public InscricaoResponseDTO atualizar(
            String email,
            InscricaoEdicaoDTO dados
    ) {
        Inscricao inscricao = buscarEntidadePorEmail(email);

        String nome = regras.normalizarNome(dados.nome());

        regras.validarParticipacao(
                dados.dataNascimento(),
                dados.percurso()
        );

        inscricao.setNome(nome);
        inscricao.setDataNascimento(dados.dataNascimento());
        inscricao.setPercurso(dados.percurso());

        return converterParaResponse(repository.save(inscricao));
    }

    @Transactional
    public void cancelar(String email) {
        Inscricao inscricao = buscarEntidadePorEmail(email);
        repository.delete(inscricao);
    }

    @Transactional(readOnly = true)
    public ResumoDTO obterResumo() {
        long total = repository.count();
        long km5 = repository.countByPercurso(Percurso.KM_5);
        long km10 = repository.countByPercurso(Percurso.KM_10);

        return new ResumoDTO(total, km5, km10);
    }

    private Inscricao buscarEntidadePorEmail(String email) {
        String emailNormalizado = regras.normalizarEmail(email);

        return repository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new NoSuchElementException(
                        "Inscrição não encontrada."
                ));
    }

    private InscricaoResponseDTO converterParaResponse(Inscricao inscricao) {
        int idade = regras.calcularIdadeNaProva(
                inscricao.getDataNascimento()
        );

        Categoria categoria = regras.calcularCategoria(
                inscricao.getDataNascimento()
        );

        return new InscricaoResponseDTO(
                inscricao.getNome(),
                inscricao.getEmail(),
                inscricao.getDataNascimento(),
                inscricao.getPercurso(),
                idade,
                formatarCategoria(categoria)
        );
    }

    private String formatarCategoria(Categoria categoria) {
        return switch (categoria) {
            case JUVENIL -> "Juvenil";
            case ADULTO -> "Adulto";
            case MASTER -> "Master";
            case SENIOR -> "Senior";
        };
    }
}
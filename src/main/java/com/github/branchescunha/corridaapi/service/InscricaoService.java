package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.dto.InscricaoCadastroDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoEdicaoDTO;
import com.github.branchescunha.corridaapi.dto.InscricaoResponseDTO;
import com.github.branchescunha.corridaapi.dto.ResumoDTO;
import com.github.branchescunha.corridaapi.entity.Inscricao;
import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.enums.Percurso;
import com.github.branchescunha.corridaapi.repository.InscricaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InscricaoService {

    private final InscricaoRepository repository;
    private final RegraInscricaoService regraInscricaoService;

    public InscricaoService(
            InscricaoRepository repository,
            RegraInscricaoService regraInscricaoService
    ) {
        this.repository = repository;
        this.regraInscricaoService = regraInscricaoService;
    }

    public InscricaoResponseDTO cadastrar(
            InscricaoCadastroDTO dados
    ) {
        String nome = regraInscricaoService.normalizarNome(
                dados.nome()
        );

        String email = regraInscricaoService.normalizarEmail(
                dados.email()
        );

        regraInscricaoService.validarParticipacao(
                dados.dataNascimento(),
                dados.percurso()
        );

        if (repository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "O e-mail já possui uma inscrição ativa."
            );
        }

        Inscricao inscricao = new Inscricao(
                nome,
                email,
                dados.dataNascimento(),
                dados.percurso()
        );

        Inscricao salva = repository.save(inscricao);

        return converterParaResponse(salva);
    }

    public List<InscricaoResponseDTO> listar() {
        return repository
                .findAllByOrderByIdAsc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public InscricaoResponseDTO buscarPorEmail(String email) {
        return converterParaResponse(
                buscarEntidadePorEmail(email)
        );
    }

    public InscricaoResponseDTO atualizar(
            String email,
            InscricaoEdicaoDTO dados
    ) {
        Inscricao inscricao = buscarEntidadePorEmail(email);

        /*
         * Todas as validações são executadas antes de alterar
         * a entidade. Assim, uma edição recusada preserva
         * integralmente os dados anteriores.
         */
        String nomeNormalizado =
                regraInscricaoService.normalizarNome(
                        dados.nome()
                );

        regraInscricaoService.validarParticipacao(
                dados.dataNascimento(),
                dados.percurso()
        );

        inscricao.setNome(nomeNormalizado);
        inscricao.setDataNascimento(dados.dataNascimento());
        inscricao.setPercurso(dados.percurso());

        Inscricao atualizada = repository.save(inscricao);

        return converterParaResponse(atualizada);
    }

    public void cancelar(String email) {
        repository.delete(
                buscarEntidadePorEmail(email)
        );
    }

    public ResumoDTO obterResumo() {
        long total = repository.count();

        long km5 = repository.countByPercurso(
                Percurso.KM_5
        );

        long km10 = repository.countByPercurso(
                Percurso.KM_10
        );

        return new ResumoDTO(
                total,
                km5,
                km10
        );
    }

    private Inscricao buscarEntidadePorEmail(String email) {
        String emailNormalizado =
                regraInscricaoService.normalizarEmail(email);

        return repository
                .findByEmail(emailNormalizado)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Não existe inscrição ativa para o e-mail informado."
                        )
                );
    }

    private InscricaoResponseDTO converterParaResponse(
            Inscricao inscricao
    ) {
        int idade = regraInscricaoService.calcularIdadeNaProva(
                inscricao.getDataNascimento()
        );

        Categoria categoria =
                regraInscricaoService.calcularCategoria(
                        inscricao.getDataNascimento()
                );

        return new InscricaoResponseDTO(
                inscricao.getNome(),
                inscricao.getEmail(),
                inscricao.getDataNascimento(),
                inscricao.getPercurso(),
                idade,
                categoria.name()
        );
    }
}
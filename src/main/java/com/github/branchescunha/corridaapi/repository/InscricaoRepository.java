package com.github.branchescunha.corridaapi.repository;

import com.github.branchescunha.corridaapi.entity.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    Optional<Inscricao> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Inscricao> findAllByOrderByIdAsc();
}
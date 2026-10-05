package com.fsa.gestao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsa.gestao.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByNomeContainingIgnoreCase(String nome);

    List<Professor> findByAreaIgnoreCase(String area);
}

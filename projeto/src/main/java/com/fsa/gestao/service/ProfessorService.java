package com.fsa.gestao.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fsa.gestao.model.Professor;
import com.fsa.gestao.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    public List<Professor> buscarPorNome(String nome) {
        return professorRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Professor> buscarPorArea(String area) {
        return professorRepository.findByAreaIgnoreCase(area);
    }

    public Professor cadastrar(Professor professor) {
        professor.setId(null);
        return professorRepository.save(professor);
    }

    public Professor editar(Long id, Professor dados) {
        Professor professor = buscarPorId(id);
        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());
        professor.setArea(dados.getArea());
        professor.setTelefone(dados.getTelefone());
        return professorRepository.save(professor);
    }

    public void excluir(Long id) {
        Professor professor = buscarPorId(id);
        professorRepository.delete(professor);
    }

    private Professor buscarPorId(Long id) {
        return professorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
    }
}

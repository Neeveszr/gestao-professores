package com.fsa.gestao.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fsa.gestao.model.Professor;
import com.fsa.gestao.service.ProfessorService;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<Professor> listarProfessores() {
        return professorService.listarTodos();
    }

    @GetMapping("/nome/{nome}")
    public List<Professor> buscarPorNome(@PathVariable String nome) {
        return professorService.buscarPorNome(nome);
    }

    @GetMapping("/area/{area}")
    public List<Professor> buscarPorArea(@PathVariable String area) {
        return professorService.buscarPorArea(area);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Professor cadastrarProfessor(@RequestBody Professor professor) {
        return professorService.cadastrar(professor);
    }

    @PutMapping("/{id}")
    public Professor editarProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        return professorService.editar(id, professor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProfessor(@PathVariable Long id) {
        professorService.excluir(id);
    }
}

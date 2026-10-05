package com.fsa.gestao;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fsa.gestao.controller.ProfessorController;
import com.fsa.gestao.model.Professor;
import com.fsa.gestao.repository.ProfessorRepository;
import com.fsa.gestao.service.ProfessorService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfessorControllerTests {

    private ProfessorRepository repository;
    private MockMvc api;

    @BeforeEach
    void configurar() {
        repository = mock(ProfessorRepository.class);
        api = MockMvcBuilders.standaloneSetup(
                new ProfessorController(new ProfessorService(repository))).build();
    }

    @Test
    void listarProfessores() throws Exception {
        when(repository.findAll()).thenReturn(List.of(professor()));
        api.perform(get("/professores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"));
    }

    @Test
    void filtrarNomeUsandoConsultaParcialSemDiferenciarMaiusculas() throws Exception {
        when(repository.findByNomeContainingIgnoreCase("mAR"))
                .thenReturn(List.of(professor()));
        api.perform(get("/professores/nome/mAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"));
        verify(repository).findByNomeContainingIgnoreCase("mAR");
    }

    @Test
    void filtrarAreaUsandoConsultaSemDiferenciarMaiusculas() throws Exception {
        when(repository.findByAreaIgnoreCase("desenvolvimento"))
                .thenReturn(List.of(professor()));
        api.perform(get("/professores/area/desenvolvimento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].area").value("Desenvolvimento"));
        verify(repository).findByAreaIgnoreCase("desenvolvimento");
    }

    @Test
    void cadastrarSemPermitirSobrescreverIdEnviadoNoCorpo() throws Exception {
        when(repository.save(any(Professor.class))).thenAnswer(invocacao -> {
            Professor salvo = invocacao.getArgument(0);
            assertNull(salvo.getId());
            salvo.setId(4L);
            return salvo;
        });
        api.perform(post("/professores").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id":1,"nome":"Maria Silva","email":"maria@example.com",
                     "area":"Desenvolvimento","telefone":"86999999999"}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@example.com"))
                .andExpect(jsonPath("$.area").value("Desenvolvimento"))
                .andExpect(jsonPath("$.telefone").value("86999999999"));
    }

    @Test
    void editarProfessorIdentificadoNaUrl() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(professor()));
        when(repository.save(any(Professor.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
        api.perform(put("/professores/1").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id":999,"nome":"Maria Silva Santos","email":"santos@example.com",
                     "area":"Engenharia de Software","telefone":"86988888888"}
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva Santos"))
                .andExpect(jsonPath("$.email").value("santos@example.com"))
                .andExpect(jsonPath("$.area").value("Engenharia de Software"))
                .andExpect(jsonPath("$.telefone").value("86988888888"));
    }

    @Test
    void excluirProfessor() throws Exception {
        Professor professor = professor();
        when(repository.findById(1L)).thenReturn(Optional.of(professor));
        api.perform(delete("/professores/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(repository).delete(professor);
    }

    @Test
    void editarProfessorInexistenteRetorna404() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        api.perform(put("/professores/999").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Maria\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluirProfessorInexistenteRetorna404() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        api.perform(delete("/professores/999"))
                .andExpect(status().isNotFound());
    }

    private Professor professor() {
        Professor professor = new Professor();
        professor.setId(1L);
        professor.setNome("Maria Silva");
        professor.setEmail("maria@example.com");
        professor.setArea("Desenvolvimento");
        professor.setTelefone("86999999999");
        return professor;
    }
}

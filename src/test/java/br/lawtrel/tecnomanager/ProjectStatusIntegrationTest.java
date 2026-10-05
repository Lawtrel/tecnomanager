package br.lawtrel.tecnomanager;

import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.model.Task;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.test.context.ContextConfiguration(initializers = TestDatabaseInitializer.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectStatusIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ProjectRepository projects;
    @Autowired TaskRepository tasks;

    private Project projectWithTask(String taskStatus) {
        Project project = projects.save(new Project("API", "Projeto de teste", "EM_ANDAMENTO"));
        Task task = new Task("Entregar API", "", taskStatus, null);
        task.setProject(project);
        tasks.saveAndFlush(task);
        return project;
    }

    @ParameterizedTest
    @ValueSource(strings = {"PENDENTE", "EM_ANDAMENTO"})
    void pendingTaskBlocksCompletion(String taskStatus) throws Exception {
        Project project = projectWithTask(taskStatus);
        mvc.perform(patch("/projetos/{id}/status", project.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Não é possível concluir o projeto pois ainda existem tarefas pendentes."));
        assertEquals("EM_ANDAMENTO", projects.findById(project.getId()).orElseThrow().getStatus());
    }

    @Test
    void completedTasksAllowNormalizedStatus() throws Exception {
        Project project = projectWithTask("CONCLUIDO");
        mvc.perform(patch("/projetos/{id}/status", project.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\" concluido \"}"))
                .andExpect(status().isNoContent());
        assertEquals("CONCLUIDO", projects.findById(project.getId()).orElseThrow().getStatus());
    }

    @Test
    void noTasksAllowCompletion() throws Exception {
        Project project = projects.save(new Project("API", "", "EM_ANDAMENTO"));
        mvc.perform(patch("/projetos/{id}/status", project.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isNoContent());
        assertEquals("CONCLUIDO", projects.findById(project.getId()).orElseThrow().getStatus());
    }

    @Test
    void missingProjectReturns404() throws Exception {
        mvc.perform(patch("/projetos/{id}/status", Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"status\":null}", "{\"status\":\"\"}",
            "{\"status\":\"  \"}", "{\"status\":\"INVALIDO\"}"})
    void invalidStatusReturns400WithoutChangingProject(String body) throws Exception {
        Project project = projectWithTask("PENDENTE");
        mvc.perform(patch("/projetos/{id}/status", project.getId())
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        assertEquals("EM_ANDAMENTO", projects.findById(project.getId()).orElseThrow().getStatus());
    }

    @Test
    void invalidStatusCannotBeUsedAtCreation() throws Exception {
        long before = projects.count();
        mvc.perform(post("/projetos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"API\",\"status\":\"INVALIDO\"}"))
                .andExpect(status().isBadRequest());
        assertEquals(before, projects.count());
    }

    @Test
    void seedEndpointIsAbsentFromTestProfile() throws Exception {
        mvc.perform(post("/seed")).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{", "null", ""})
    void malformedBodyReturns400(String body) throws Exception {
        mvc.perform(patch("/projetos/1/status").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}

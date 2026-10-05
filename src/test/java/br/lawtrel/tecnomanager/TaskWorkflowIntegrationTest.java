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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestDatabaseInitializer.class)
@Transactional
class TaskWorkflowIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ProjectRepository projects;
    @Autowired TaskRepository tasks;
    @Autowired DataSource database;

    private Project project() { return projects.saveAndFlush(new Project("API", "Teste", "EM_ANDAMENTO")); }
    private Task task(Project p) {
        Task t = new Task("Implementar", "", "PENDENTE", null);
        t.setProject(p);
        return tasks.saveAndFlush(t);
    }

    @Test void databaseEngineMatchesRequestedMode() throws Exception {
        try (var connection = database.getConnection()) {
            String expected = System.getenv("TEST_JDBC_URL") == null ? "H2" : "PostgreSQL";
            assertEquals(expected, connection.getMetaData().getDatabaseProductName());
        }
    }

    @Test void cannotChangeTaskThroughAnotherProject() throws Exception {
        Project original = project(), other = project();
        Task t = task(original);
        mvc.perform(patch("/projetos/{p}/tarefas/{t}/status", other.getId(), t.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isNotFound());
        assertEquals("PENDENTE", tasks.findById(t.getId()).orElseThrow().getStatus());
    }

    @ParameterizedTest @ValueSource(strings = {"{}", "{\"status\":null}", "{\"status\":\"\"}", "{\"status\":\"DESCONHECIDO\"}"})
    void rejectsInvalidStatusWithoutChangingTask(String payload) throws Exception {
        Project p = project(); Task t = task(p);
        mvc.perform(patch("/projetos/{p}/tarefas/{t}/status", p.getId(), t.getId())
                .contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isBadRequest());
        assertEquals("PENDENTE", tasks.findById(t.getId()).orElseThrow().getStatus());
    }

    @Test void normalizedCompletionAllowsProjectCompletion() throws Exception {
        Project p = project(); Task t = task(p);
        mvc.perform(patch("/projetos/{p}/tarefas/{t}/status", p.getId(), t.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\" concluida \"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDO"));
        mvc.perform(patch("/projetos/{id}/status", p.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"CONCLUIDO\"}")).andExpect(status().isNoContent());
    }

    @ParameterizedTest @ValueSource(strings = {"{\"titulo\":\"\"}", "{\"titulo\":\"Tarefa\",\"status\":\"DESCONHECIDO\"}"})
    void rejectsInvalidTaskCreation(String payload) throws Exception {
        Project p = project(); long count = tasks.count();
        mvc.perform(post("/projetos/{p}/tarefas", p.getId()).contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest());
        assertEquals(count, tasks.count());
    }

    @Test void omittedStatusDefaultsToPending() throws Exception {
        Project p = project();
        mvc.perform(post("/projetos/{p}/tarefas", p.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Implementar API\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PENDENTE"));
    }
}

package br.lawtrel.tecnomanager;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestDatabaseInitializer.class)
class TaskStatusMigrationIntegrationTest {
    @Autowired DataSource database;

    @Test void upgradesLegacyRowsAndEnforcesCanonicalStatuses() throws Exception {
        // Separate schema inside the already guarded, disposable test database.
        String schema = "tm_status_test_" + UUID.randomUUID().toString().replace("-", "");
        Flyway oldVersion = Flyway.configure().dataSource(database).schemas(schema)
                .defaultSchema(schema).target("2").load();
        try {
            oldVersion.migrate();
            try (var connection = database.getConnection()) {
                connection.setSchema(schema);
                try (var statement = connection.createStatement()) {
                    statement.executeUpdate("INSERT INTO project (id, nome, status) VALUES (1, 'Legado', 'EM_ANDAMENTO')");
                    statement.executeUpdate("INSERT INTO task (id, titulo, status, project_id) VALUES "
                            + "(1, 'Alias', 'CONCLUIDA', 1), (2, 'Sem status', NULL, 1), "
                            + "(3, 'Desconhecido', 'OUTRO', 1), (4, 'Normalizar', ' concluido ', 1), "
                            + "(5, 'Em andamento', ' em_andamento ', 1), (6, 'Vazio', '', 1)");
                }
            }
            Flyway.configure().dataSource(database).schemas(schema).defaultSchema(schema).load().migrate();
            try (var connection = database.getConnection()) {
                connection.setSchema(schema);
                try (var statement = connection.createStatement()) {
                    List<String> statuses = new ArrayList<>();
                    try (var result = statement.executeQuery("SELECT status FROM task ORDER BY id")) {
                        while (result.next()) statuses.add(result.getString(1));
                    }
                    assertEquals(List.of("CONCLUIDO", "PENDENTE", "PENDENTE", "CONCLUIDO", "EM_ANDAMENTO", "PENDENTE"), statuses);
                    try (var result = statement.executeQuery("SELECT COUNT(*) FROM task WHERE project_id = 1 AND status <> 'CONCLUIDO'")) {
                        assertTrue(result.next());
                        assertEquals(4, result.getInt(1), "Legacy unknown/null tasks must block completion");
                    }
                    assertThrows(SQLException.class, () -> statement.executeUpdate("UPDATE task SET status = NULL WHERE id = 1"));
                    assertThrows(SQLException.class, () -> statement.executeUpdate("UPDATE task SET status = 'CONCLUIDA' WHERE id = 1"));
                    assertThrows(SQLException.class, () -> statement.executeUpdate("INSERT INTO task (id, titulo, status, project_id) VALUES (7, 'Invalida', 'OUTRO', 1)"));
                    assertEquals(1, statement.executeUpdate("UPDATE task SET status = 'EM_ANDAMENTO' WHERE id = 1"));
                }
            }
        } finally {
            try (var connection = database.getConnection(); var statement = connection.createStatement()) {
                statement.execute("DROP SCHEMA IF EXISTS " + schema + " CASCADE");
            }
        }
    }
}

package br.lawtrel.tecnomanager.repository;

import br.lawtrel.tecnomanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Buscar tarefas de um projeto específico
    List<Task> findByProjectId(Long projectId);
}
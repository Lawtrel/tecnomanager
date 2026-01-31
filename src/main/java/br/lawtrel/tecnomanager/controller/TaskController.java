package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.dto.TaskDTO;
import br.lawtrel.tecnomanager.model.Task;
import br.lawtrel.tecnomanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos/{projetoId}/tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Cria uma nova tarefa no projeto")
    @PostMapping
    public ResponseEntity<Task> criar(@PathVariable Long projetoId, @RequestBody TaskDTO dados) {
        Task task = taskService.criarTarefa(projetoId, dados);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Lista todas as tarefas de um projeto")
    @GetMapping
    public ResponseEntity<List<Task>> listar(@PathVariable Long projetoId) {
        return ResponseEntity.ok(taskService.listarPorProjeto(projetoId));
    }
}
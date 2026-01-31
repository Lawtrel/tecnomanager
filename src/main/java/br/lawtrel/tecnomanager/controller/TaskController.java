package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.dto.TaskDTO;
import br.lawtrel.tecnomanager.model.Task;
import br.lawtrel.tecnomanager.service.TaskService;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


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

    @Operation(summary = "Atualiza o status da tarefa")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String novoStatus = payload.get("status");
        Task task = taskService.atualizarStatus(id, novoStatus); // Crie esse método no Service conforme acima
        return ResponseEntity.ok(task);
    }
}
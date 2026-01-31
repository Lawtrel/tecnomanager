package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.dto.ProjectDTO;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projetos")

public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Cria um novo projeto", description = "Cadastra um projeto no banco com status inicial PLANEJAMENTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })


    //Cria um novo projeto
    @PostMapping
    public Project create(@RequestBody @Valid ProjectDTO dados) {
        return projectService.criar(dados);
    }

    @GetMapping
    public List<Project> list() {
        return projectService.listarTodos();
    }

    //Adicionar membro ao projeto
    @PostMapping("/{idProjeto}/membros/{idMembro}")
    public Project addMember(@PathVariable Long idProjeto, @PathVariable Long idMembro) {
        return projectService.alocarMembro(idProjeto, idMembro);
    }

    @Operation(summary = "Atualiza o status do projeto", description = "Transições para CONCLUIDO só são permitidas se todas as tarefas estiverem finalizadas.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        // Extrai o status do JSON {"status": "CONCLUIDO"}
        String novoStatus = payload.get("status");
        projectService.atualizarStatus(id, novoStatus);
        return ResponseEntity.noContent().build();
    }



}

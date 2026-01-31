package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.dto.ProjectDTO;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projetos")

public class ProjectController {

    @Autowired
    private ProjectService projectService;

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



}

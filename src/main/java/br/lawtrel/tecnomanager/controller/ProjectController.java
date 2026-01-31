package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projetos")

public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    //Cria um novo projeto
    @PostMapping
    public Project create(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    //Lista projetos
    @GetMapping
    public List<Project> list() {
        return projectRepository.findAll();
    }

    //Adicionar membro ao projeto
    @PostMapping("/{idProjeto}/membros/{idMembro}")
    public Project addMember(@PathVariable Long idProjeto, @PathVariable Long idMembro) {

        Project project = projectRepository.findById(idProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Member member = memberRepository.findById(idMembro)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        // Adiciona nas listas
        project.getMembers().add(member);
        member.getProjects().add(project);

        return projectRepository.save(project);
    }



}

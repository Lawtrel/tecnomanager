package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.dto.ProjectDTO;
import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Project criar(ProjectDTO dados) {
        Project project = new Project();
        project.setNome(dados.nome());
        project.setDescricao(dados.descricao());
        project.setStatus(dados.status());

        return projectRepository.save(project);
    }

    public List<Project> listarTodos() {
        return projectRepository.findAll();
    }

    public Project alocarMembro(Long idProjeto, Long idMembro) {
        //Buscar
        Project project = projectRepository.findById(idProjeto)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        //Buscar membro
        Member member = memberRepository.findById(idMembro)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        //Faz o Vínculo
        project.getMembers().add(member);
        member.getProjects().add(project);

        //Salva
        return projectRepository.save(project);

    }
}

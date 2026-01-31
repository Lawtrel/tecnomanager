package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.dto.ProjectDTO;
import br.lawtrel.tecnomanager.exception.BusinessRuleException;
import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.lawtrel.tecnomanager.exception.ResourceNotFoundException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TaskRepository taskRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado"));

        //Buscar membro
        Member member = memberRepository.findById(idMembro)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado"));

        //Faz o Vínculo
        project.getMembers().add(member);
        member.getProjects().add(project);

        //Salva
        return projectRepository.save(project);

    }

    public void atualizarStatus(Long id, String novoStatus) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com ID: " + id));

        // Normaliza para maiúsculo (evita erro se mandarem "concluido")
        String statusNormalizado = novoStatus.toUpperCase();

        if ("CONCLUIDO".equals(statusNormalizado)) {
            // ...verifica se existe alguma tarefa que NÃO seja "CONCLUIDO"
            boolean temTarefasPendentes = taskRepository.existsByProjectIdAndStatusNot(id, "CONCLUIDO");

            if (temTarefasPendentes) {
                throw new BusinessRuleException("Não é possível concluir o projeto pois ainda existem tarefas pendentes.");
            }
        }

        project.setStatus(statusNormalizado);
        projectRepository.save(project);
    }
}

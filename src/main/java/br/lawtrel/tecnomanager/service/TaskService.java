package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.dto.TaskDTO;
import br.lawtrel.tecnomanager.exception.ResourceNotFoundException;
import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.model.Task;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    public Task criarTarefa(Long projetoId, TaskDTO dados) {
        // Busca o projeto
        Project project = projectRepository.findById(projetoId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado"));

        // Cria a entidade Task
        Task task = new Task();
        task.setTitulo(dados.titulo());
        task.setDescricao(dados.descricao());
        task.setStatus(dados.status() != null ? dados.status() : "PENDENTE");
        task.setDataLimite(dados.dataLimite());
        task.setProject(project);

        // Se veio ID de membro, busca e associa
        if (dados.membroId() != null) {
            Member member = memberRepository.findById(dados.membroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado"));

            task.setMember(member);
        }

        return taskRepository.save(task);
    }

    public List<Task> listarPorProjeto(Long projetoId) {
        if (!projectRepository.existsById(projetoId)) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }
        return taskRepository.findByProjectId(projetoId);
    }

    public Task atualizarStatus(Long id, String novoStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
        task.setStatus(novoStatus);
        return taskRepository.save(task);
    }
}
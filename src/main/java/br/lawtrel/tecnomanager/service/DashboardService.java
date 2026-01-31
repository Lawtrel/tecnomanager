package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.dto.DashboardDTO;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    public DashboardService(ProjectRepository projectRepository,
                            TaskRepository taskRepository,
                            MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.memberRepository = memberRepository;
    }

    public DashboardDTO gerarRelatorio() {
        // 1. Busca projetos 'INICIADO' e 'EM_ANDAMENTO'
        long projetosAtivos = projectRepository.countByStatusIn(Arrays.asList("INICIADO", "EM_ANDAMENTO"));

        // 2. Busca todas as tarefas que NÃO estão 'CONCLUIDO'
        long tarefasPendentes = taskRepository.countByStatusNot("CONCLUIDO");

        // 3. Busca membros ociosos
        long membrosLivres = memberRepository.countMembrosSemTarefas();

        return new DashboardDTO(projetosAtivos, tarefasPendentes, membrosLivres);
    }
}
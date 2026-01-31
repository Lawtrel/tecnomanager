package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.model.Task;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import br.lawtrel.tecnomanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DBSeedingService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    public DBSeedingService(ProjectRepository projectRepository, MemberRepository memberRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void popularBanco() {
        // 1. Limpa tudo (Ordem importa por causa das chaves estrangeiras!)
        taskRepository.deleteAll();
        List<Project> projetos = projectRepository.findAll();
        for (Project p : projetos) {
            p.getMembers().clear(); // Esvazia a lista de membros do projeto
            projectRepository.save(p); // O JPA remove as linhas da tabela 'project_members'
        }
        projectRepository.deleteAll(); // Apaga Projetos
        memberRepository.deleteAll();  // Apaga Membros

        // 2. Cria Membros
        Member m1 = new Member("Alice Dev", "Backend Dev", "alice@tecno.com");
        Member m2 = new Member("Bob Design","UX/UI","bob@tecno.com");
        Member m3 = new Member("Carol Lead","Gerente","carol@tecno.com");
        memberRepository.saveAll(List.of(m1, m2, m3));

        // 3. Cria Projetos
        Project p1 = new Project("App Delivery", "Aplicativo estilo iFood", "EM_ANDAMENTO");
        Project p2 = new Project("Site Institucional", "Landing page da empresa", "INICIADO");
        projectRepository.saveAll(List.of(p1, p2));

        // 4. Cria Tarefas e Vínculos
        // Tarefa 1: App Delivery (Pendente)
        Task t1 = new Task("Criar API Login", "Autenticação com JWT", "PENDENTE", LocalDateTime.now().plusDays(2));
        t1.setProject(p1);
        t1.setMember(m1); // Alice

        // Tarefa 2: App Delivery (Concluida)
        Task t2 = new Task("Desenhar Telas", "Figma das telas", "CONCLUIDO", LocalDateTime.now().minusDays(1));
        t2.setProject(p1);
        t2.setMember(m2); // Bob

        // Tarefa 3: Site (Pendente e Atrasada)
        Task t3 = new Task("Comprar Dominio", "Registro.br", "PENDENTE", LocalDateTime.now().minusDays(5));
        t3.setProject(p2);

        Task t4 = new Task("Configurar Banco", "Postgres no Docker", "CONCLUIDO", LocalDateTime.now().minusDays(2));
        t4.setProject(p1);
        t4.setMember(m1);

        Task t5 = new Task("Reunião de Kickoff", "Alinhamento com cliente", "CONCLUIDO", LocalDateTime.now().minusDays(10));
        t5.setProject(p2);
        t5.setMember(m3);

        taskRepository.saveAll(List.of(t1, t2, t3, t4, t5));

        // Vínculo Projeto <-> Membro (Tabela de Junção)
        p1.getMembers().add(m1); m1.getProjects().add(p1);
        p1.getMembers().add(m2); m2.getProjects().add(p1);
        p2.getMembers().add(m3); m3.getProjects().add(p2);

        projectRepository.save(p1);
        projectRepository.save(p2);
    }
}
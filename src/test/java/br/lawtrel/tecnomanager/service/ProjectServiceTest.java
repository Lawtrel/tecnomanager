package br.lawtrel.tecnomanager.service;

import br.lawtrel.tecnomanager.dto.ProjectDTO;
import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.model.Project;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import br.lawtrel.tecnomanager.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ProjectService projectService;

    // --- TESTE DO MÉTODO CRIAR ---
    @Test
    @DisplayName("Deve criar um projeto com sucesso a partir do DTO")
    void deveCriarProjetoComSucesso() {
        // 1. ARRANGE (Preparar o cenário)
        ProjectDTO dto = new ProjectDTO("Novo Projeto", "Descricao Teste", "EM_PLANEJAMENTO");

        // Quando o repository salvar qualquer projeto, retorne um projeto com ID 1
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            p.setId(1L); // Simulamos que o banco gerou um ID
            return p;
        });

        // 2. ACT (Executar a ação real)
        Project resultado = projectService.criar(dto);

        // 3. ASSERT (Verificar se deu certo)
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Novo Projeto", resultado.getNome());
        assertEquals("Descricao Teste", resultado.getDescricao());
        assertEquals("EM_PLANEJAMENTO", resultado.getStatus());

        // Garante que o método save foi chamado 1 vez
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    // --- TESTE DO MÉTODO LISTAR ---
    @Test
    @DisplayName("Deve retornar lista de projetos")
    void deveListarTodosProjetos() {
        // 1. ARRANGE
        // Criamos uma lista falsa com 2 projetos
        List<Project> listaMock = List.of(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(listaMock);

        // 2. ACT
        List<Project> resultado = projectService.listarTodos();

        // 3. ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(projectRepository, times(1)).findAll();
    }

    // --- TESTES DO MÉTODO ALOCAR (Que já fizemos) ---
    @Test
    @DisplayName("Deve alocar membro ao projeto com sucesso")
    void deveAlocarMembroComSucesso() {
        Long idProjeto = 1L;
        Long idMembro = 2L;

        Project projetoMock = new Project();
        projetoMock.setId(idProjeto);
        projetoMock.setMembers(new ArrayList<>());

        Member membroMock = new Member();
        membroMock.setId(idMembro);
        membroMock.setProjects(new ArrayList<>());

        when(projectRepository.findById(idProjeto)).thenReturn(Optional.of(projetoMock));
        when(memberRepository.findById(idMembro)).thenReturn(Optional.of(membroMock));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project resultado = projectService.alocarMembro(idProjeto, idMembro);

        assertNotNull(resultado);
        assertEquals(1, resultado.getMembers().size());
        assertEquals(idMembro, resultado.getMembers().get(0).getId()); // O membro na lista é o esperado?
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar alocar em projeto inexistente")
    void deveLancarErroQuandoProjetoNaoExiste() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.alocarMembro(99L, 1L);
        });

        assertEquals("Projeto não encontrado", exception.getMessage());
    }
}
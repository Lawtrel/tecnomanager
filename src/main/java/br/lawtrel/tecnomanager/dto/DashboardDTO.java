package br.lawtrel.tecnomanager.dto;

public record DashboardDTO(
        long totalProjetosAtivos,
        long totalTarefasPendentes,
        long membrosSemTarefas
) {}
package br.lawtrel.tecnomanager.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;

public record TaskDTO(
        @NotBlank(message = "O título da tarefa é obrigatório.") String titulo,
        String descricao,
        String status,
        LocalDateTime dataLimite,
        Long membroId // Opcional: ID do membro responsável
) {}

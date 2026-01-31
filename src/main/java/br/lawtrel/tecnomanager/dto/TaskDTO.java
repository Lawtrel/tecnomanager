package br.lawtrel.tecnomanager.dto;

import java.time.LocalDateTime;

public record TaskDTO(
        String titulo,
        String descricao,
        String status,
        LocalDateTime dataLimite,
        Long membroId // Opcional: ID do membro responsável
) {}
package br.lawtrel.tecnomanager.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectDTO(
        @NotBlank
        String nome,


        String descricao,

        @NotBlank
        String status
) {}

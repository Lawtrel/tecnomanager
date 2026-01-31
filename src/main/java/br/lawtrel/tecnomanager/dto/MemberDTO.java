package br.lawtrel.tecnomanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @NotBlank
        String cargo,

        @Email(message = "Formato de e-mail inválido")
        String email
) {}

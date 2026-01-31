package br.lawtrel.tecnomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cargo;
    private String email;

    @ManyToMany
    @JoinTable(
            name = "member_project", // Nome da tabela intermediária que o Java vai criar
            joinColumns = @JoinColumn(name = "member_id"), // Coluna do Membro
            inverseJoinColumns = @JoinColumn(name = "project_id") // Coluna do Projeto
    )
    @JsonIgnore

    private List<Project> projects;

    public List<Project> getProjects() {
        return this.projects;
    }
}

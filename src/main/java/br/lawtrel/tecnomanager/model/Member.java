package br.lawtrel.tecnomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}

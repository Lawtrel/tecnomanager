package br.lawtrel.tecnomanager.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private String status;

    @ManyToMany(mappedBy = "projects")
    private List<Member> members;

    public List<Member> getMembers() {
        return this.members;
    }
}

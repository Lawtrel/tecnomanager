package br.lawtrel.tecnomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;
    private String descricao;
    private String status; // PENDENTE, EM_ANDAMENTO, CONCLUIDA
    private LocalDateTime dataLimite;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore // Evita loop infinito ao serializar
    private Project project;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // --- Auditoria ---
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    private LocalDateTime ultimaAtualizacao;

    // --- Construtores ---
    public Task() {
    }

    public Task(String titulo, String descricao, String status, LocalDateTime dataLimite) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataLimite = dataLimite;
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDateTime dataLimite) { this.dataLimite = dataLimite; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }
}
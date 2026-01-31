CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    status VARCHAR(50), -- PENDENTE, EM_ANDAMENTO, CONCLUIDA
    data_limite TIMESTAMP,
    project_id BIGINT NOT NULL,
    member_id BIGINT, -- Pode ser NULL (tarefa sem dono ainda)

    -- Campos de Auditoria
    data_criacao TIMESTAMP,
    ultima_atualizacao TIMESTAMP,

    -- Chaves Estrangeiras
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_task_member FOREIGN KEY (member_id) REFERENCES member(id)
);
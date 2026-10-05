-- Preserve completion aliases; unknown or missing states remain pending for review.
UPDATE task
SET status = CASE
    WHEN UPPER(TRIM(status)) IN ('CONCLUIDA', 'CONCLUIDO') THEN 'CONCLUIDO'
    WHEN UPPER(TRIM(status)) = 'EM_ANDAMENTO' THEN 'EM_ANDAMENTO'
    ELSE 'PENDENTE'
END;

ALTER TABLE task ALTER COLUMN status SET NOT NULL;
ALTER TABLE task ADD CONSTRAINT ck_task_status
    CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDO'));

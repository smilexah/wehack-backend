-- Добавляем колонку used_at для хранения времени использования билета
ALTER TABLE tickets
    ADD COLUMN used_at TIMESTAMP;

COMMENT ON COLUMN tickets.used_at IS 'Дата и время использования билета (отметка check-in)';

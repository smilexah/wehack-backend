CREATE TABLE clubs (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE,
                       description TEXT,
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE clubs IS 'Таблица студенческих клубов';
COMMENT ON COLUMN clubs.name IS 'Уникальное название клуба';
COMMENT ON COLUMN clubs.description IS 'Описание клуба';
COMMENT ON COLUMN clubs.created_at IS 'Дата создания записи клуба';

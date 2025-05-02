CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON TABLE roles IS 'List of available roles in the system (e.g., USER, ADMIN)';
COMMENT ON COLUMN roles.id IS 'Unique identifier for the role';
COMMENT ON COLUMN roles.name IS 'Role name used for authorization checks';

INSERT INTO roles (name)
VALUES
    ('USER'),
    ('ADMIN');
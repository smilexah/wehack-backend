CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    phone_number VARCHAR(255),
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);

COMMENT ON TABLE users IS 'Table of registered users';
COMMENT ON COLUMN users.id IS 'Primary key (user ID)';
COMMENT ON COLUMN users.email IS 'Unique email address';
COMMENT ON COLUMN users.first_name IS 'User first name';
COMMENT ON COLUMN users.last_name IS 'User last name';
COMMENT ON COLUMN users.phone_number IS 'Phone number';
COMMENT ON COLUMN users.is_active IS 'Is the user active';
COMMENT ON COLUMN users.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN users.updated_at IS 'Timestamp of last update';
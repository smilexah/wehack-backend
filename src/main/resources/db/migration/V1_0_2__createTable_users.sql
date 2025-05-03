CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    phone_number VARCHAR(255),
    tg_chat_id    BIGINT,
    tg_link_token TEXT,
    is_active    BOOLEAN   DEFAULT TRUE,
    is_verified  BOOLEAN   DEFAULT FALSE,
    verification_code VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tg_token_created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
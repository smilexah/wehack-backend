CREATE TABLE tokens
(
    id      BIGSERIAL PRIMARY KEY,
    token   TEXT   NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

COMMENT ON TABLE tokens IS 'Table storing JWT tokens for users (access & refresh)';
COMMENT ON COLUMN tokens.id IS 'Primary key of the token table';
COMMENT ON COLUMN tokens.token IS 'JWT token string (access or refresh)';
COMMENT ON COLUMN tokens.revoked IS 'Flag to indicate if the token is revoked';
COMMENT ON COLUMN tokens.expired IS 'Flag to indicate if the token is expired';
COMMENT ON COLUMN tokens.user_id IS 'Reference to the user who owns this token';

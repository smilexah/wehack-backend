CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE user_roles IS 'Mapping table linking users to their assigned roles';
COMMENT ON COLUMN user_roles.user_id IS 'ID of the user from users table';
COMMENT ON COLUMN user_roles.role_id IS 'ID of the role from roles table';

-- Таблица текущих баллов Social GPA для каждого пользователя
CREATE TABLE social_gpa (
                            user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                            total_points INT NOT NULL DEFAULT 0,
                            updated_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE social_gpa IS 'Таблица накопленных Social GPA баллов пользователей';
COMMENT ON COLUMN social_gpa.user_id IS 'Пользователь (FK -> users)';
COMMENT ON COLUMN social_gpa.total_points IS 'Общее количество Social GPA баллов';
COMMENT ON COLUMN social_gpa.updated_at IS 'Дата последнего обновления баллов';

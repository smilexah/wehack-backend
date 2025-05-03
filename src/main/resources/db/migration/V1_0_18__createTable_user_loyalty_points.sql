-- Таблица накопленных баллов лояльности пользователей
CREATE TABLE user_loyalty_points (
                                     user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                                     points INT NOT NULL DEFAULT 0,
                                     updated_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE user_loyalty_points IS 'Таблица баллов лояльности пользователей';
COMMENT ON COLUMN user_loyalty_points.user_id IS 'Пользователь (FK -> users)';
COMMENT ON COLUMN user_loyalty_points.points IS 'Накопленные баллы лояльности';
COMMENT ON COLUMN user_loyalty_points.updated_at IS 'Дата последнего обновления баллов';

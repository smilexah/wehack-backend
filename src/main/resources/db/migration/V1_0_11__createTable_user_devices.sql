-- Таблица устройств пользователей для отправки push-уведомлений:contentReference[oaicite:1]{index=1}
CREATE TABLE user_devices (
                              id SERIAL PRIMARY KEY,
                              user_id INT REFERENCES users(id) ON DELETE CASCADE,
                              device_token TEXT NOT NULL,
                              platform VARCHAR(20),
                              created_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE user_devices IS 'Таблица устройства пользователя для push-уведомлений';
COMMENT ON COLUMN user_devices.user_id IS 'Пользователь, чей это девайс (FK -> users)';
COMMENT ON COLUMN user_devices.device_token IS 'Токен устройства для push-уведомлений';
COMMENT ON COLUMN user_devices.platform IS 'Платформа устройства (iOS, Android и т.д.)';
COMMENT ON COLUMN user_devices.created_at IS 'Дата регистрации токена устройства';

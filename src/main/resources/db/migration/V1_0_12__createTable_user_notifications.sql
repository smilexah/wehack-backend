-- Таблица уведомлений для пользователей о событиях и клубах
CREATE TABLE user_notifications
(
    id                SERIAL PRIMARY KEY,
    user_id           INT REFERENCES users (id) ON DELETE CASCADE,
    title             VARCHAR(255) NOT NULL,
    message           TEXT         NOT NULL,
    event_id          INT REFERENCES events (id) ON DELETE CASCADE,
    club_id           INT REFERENCES clubs (id) ON DELETE CASCADE,
    notification_type VARCHAR(50),
    sent_at           TIMESTAMP    NOT NULL DEFAULT now(),
    read_at           TIMESTAMP
);
COMMENT ON TABLE user_notifications IS 'Таблица уведомлений для пользователей (push/уведомления)';
COMMENT ON COLUMN user_notifications.user_id IS 'Пользователь-адресат уведомления (FK -> users)';
COMMENT ON COLUMN user_notifications.title IS 'Заголовок уведомления';
COMMENT ON COLUMN user_notifications.message IS 'Текст уведомления';
COMMENT ON COLUMN user_notifications.event_id IS 'Связанное мероприятие (если применимо, FK -> events)';
COMMENT ON COLUMN user_notifications.club_id IS 'Связанный клуб (если применимо, FK -> clubs)';
COMMENT ON COLUMN user_notifications.notification_type IS 'Тип уведомления (напр., напоминание, новость клуба)';
COMMENT ON COLUMN user_notifications.sent_at IS 'Время отправки уведомления';
COMMENT ON COLUMN user_notifications.read_at IS 'Время прочтения уведомления пользователем';

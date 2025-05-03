-- Подписка пользователей на клубы (многие ко многим через вспомогательную таблицу:contentReference[oaicite:2]{index=2})
CREATE TABLE club_subscriptions
(
    user_id       INT REFERENCES users (id) ON DELETE CASCADE,
    club_id       INT REFERENCES clubs (id) ON DELETE CASCADE,
    subscribed_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, club_id)
);
COMMENT ON TABLE club_subscriptions IS 'Таблица подписок пользователей на клубы';
COMMENT ON COLUMN club_subscriptions.user_id IS 'Пользователь (FK -> users)';
COMMENT ON COLUMN club_subscriptions.club_id IS 'Клуб (FK -> clubs)';
COMMENT ON COLUMN club_subscriptions.subscribed_at IS 'Дата и время подписки на клуб';
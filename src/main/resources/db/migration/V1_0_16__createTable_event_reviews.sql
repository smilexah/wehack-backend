-- Таблица отзывов на мероприятия (один пользователь может оставить отзыв на несколько мероприятий:contentReference[oaicite:3]{index=3})
CREATE TABLE event_reviews
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    event_id   INT REFERENCES events (id) ON DELETE CASCADE,
    rating     INT CHECK (rating >= 1 AND rating <= 5),
    comment    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (user_id, event_id)
);
COMMENT ON TABLE event_reviews IS 'Таблица отзывов и оценок пользователей о мероприятиях';
COMMENT ON COLUMN event_reviews.user_id IS 'Пользователь, оставивший отзыв (FK -> users)';
COMMENT ON COLUMN event_reviews.event_id IS 'Мероприятие, на которое оставлен отзыв (FK -> events)';
COMMENT ON COLUMN event_reviews.rating IS 'Оценка мероприятия пользователем (1-5)';
COMMENT ON COLUMN event_reviews.comment IS 'Текст отзыва';
COMMENT ON COLUMN event_reviews.created_at IS 'Дата создания отзыва';

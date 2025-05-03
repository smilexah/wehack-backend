-- Таблица отзывов на клубы (один пользователь может оставить отзыв на несколько клубов:contentReference[oaicite:4]{index=4})
CREATE TABLE club_reviews
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    club_id    INT REFERENCES clubs (id) ON DELETE CASCADE,
    rating     INT CHECK (rating >= 1 AND rating <= 5),
    comment    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (user_id, club_id)
);
COMMENT ON TABLE club_reviews IS 'Таблица отзывов и оценок пользователей о клубах';
COMMENT ON COLUMN club_reviews.user_id IS 'Пользователь, оставивший отзыв (FK -> users)';
COMMENT ON COLUMN club_reviews.club_id IS 'Клуб, на который оставлен отзыв (FK -> clubs)';
COMMENT ON COLUMN club_reviews.rating IS 'Оценка клуба пользователем (1-5)';
COMMENT ON COLUMN club_reviews.comment IS 'Текст отзыва';
COMMENT ON COLUMN club_reviews.created_at IS 'Дата создания отзыва';

-- Таблица посещений мероприятий (история посещений и начисление баллов Social GPA)
CREATE TABLE event_attendance
(
    user_id        INT REFERENCES users (id) ON DELETE CASCADE,
    event_id       INT REFERENCES events (id) ON DELETE CASCADE,
    attended_at    TIMESTAMP NOT NULL DEFAULT now(),
    points_awarded INT       NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, event_id)
);
COMMENT ON TABLE event_attendance IS 'История посещений мероприятий пользователями и начисленных баллов';
COMMENT ON COLUMN event_attendance.user_id IS 'Пользователь (FK -> users)';
COMMENT ON COLUMN event_attendance.event_id IS 'Мероприятие (FK -> events)';
COMMENT ON COLUMN event_attendance.attended_at IS 'Дата и время посещения мероприятия';
COMMENT ON COLUMN event_attendance.points_awarded IS 'Баллы, начисленные за посещение';

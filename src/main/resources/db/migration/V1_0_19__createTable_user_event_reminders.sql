-- Таблица напоминаний о событиях (напр., для интеграции с календарем или push-напоминаний)
CREATE TABLE user_event_reminders
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    event_id   INT REFERENCES events (id) ON DELETE CASCADE,
    remind_at  TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE user_event_reminders IS 'Таблица пользовательских напоминаний о событиях';
COMMENT ON COLUMN user_event_reminders.user_id IS 'Пользователь (FK -> users)';
COMMENT ON COLUMN user_event_reminders.event_id IS 'Мероприятие, на которое пользователь хочет напоминание (FK -> events)';
COMMENT ON COLUMN user_event_reminders.remind_at IS 'Дата и время напоминания';
COMMENT ON COLUMN user_event_reminders.created_at IS 'Дата создания напоминания';
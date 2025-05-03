ALTER TABLE event_attendance
    ADD COLUMN checked_in_by INT REFERENCES users(id);

COMMENT ON COLUMN event_attendance.checked_in_by IS 'Пользователь, выполнивший check-in';
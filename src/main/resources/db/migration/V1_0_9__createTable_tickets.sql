-- Таблица билетов, каждый билет имеет свой QR-код
CREATE TABLE tickets (
                         id SERIAL PRIMARY KEY,
                         order_id INT REFERENCES orders(id) ON DELETE CASCADE,
                         user_id INT REFERENCES users(id) ON DELETE CASCADE,
                         event_id INT REFERENCES events(id) ON DELETE CASCADE,
                         qr_code TEXT UNIQUE NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE tickets IS 'Таблица билетов на мероприятия с QR-кодами';
COMMENT ON COLUMN tickets.order_id IS 'Связь с заказом (FK -> orders)';
COMMENT ON COLUMN tickets.user_id IS 'Владелец билета (FK -> users)';
COMMENT ON COLUMN tickets.event_id IS 'Мероприятие, на которое выдан билет (FK -> events)';
COMMENT ON COLUMN tickets.qr_code IS 'Уникальный QR-код билета';
COMMENT ON COLUMN tickets.status IS 'Статус билета (напр., действителен, использован, отменен)';
COMMENT ON COLUMN tickets.created_at IS 'Дата создания билета';

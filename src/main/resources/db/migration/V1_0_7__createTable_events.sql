CREATE TABLE events (
                        id SERIAL PRIMARY KEY,
                        club_id INT REFERENCES clubs(id) ON DELETE CASCADE,
                        venue_id INT REFERENCES venues(id) ON DELETE SET NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
--                         start_time TIMESTAMP NOT NULL,
--                         end_time TIMESTAMP,
                        date DATE NOT NULL,
                        time VARCHAR(255) NOT NULL,
                        is_online BOOLEAN NOT NULL DEFAULT false,
                        streaming_url TEXT,
                        price NUMERIC(10,2),
                        capacity INT,
                        created_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE events IS 'Таблица мероприятий, связанных с клубами и залами';
COMMENT ON COLUMN events.club_id IS 'Ссылка на организовавший клуб (FK -> clubs, при удалении клуба связанные мероприятия удаляются:contentReference[oaicite:0]{index=0})';
COMMENT ON COLUMN events.venue_id IS 'Ссылка на зал проведения (FK -> venues)';
COMMENT ON COLUMN events.title IS 'Название мероприятия';
COMMENT ON COLUMN events.description IS 'Описание мероприятия';
COMMENT ON COLUMN events.date IS 'Дата мероприятия';
COMMENT ON COLUMN events.time IS 'Время мероприятия';
COMMENT ON COLUMN events.is_online IS 'Признак онлайн-трансляции мероприятия';
COMMENT ON COLUMN events.streaming_url IS 'URL или ссылка на онлайн-трансляцию';
COMMENT ON COLUMN events.price IS 'Цена билета на мероприятие';
COMMENT ON COLUMN events.capacity IS 'Максимальное количество участников';
COMMENT ON COLUMN events.created_at IS 'Дата создания записи мероприятия';

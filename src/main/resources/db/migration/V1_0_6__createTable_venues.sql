CREATE TABLE venues (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        location VARCHAR(255),
                        capacity INT,
                        description TEXT
);
COMMENT ON TABLE venues IS 'Таблица залов/аудиторий для проведения мероприятий';
COMMENT ON COLUMN venues.name IS 'Название зала или помещения';
COMMENT ON COLUMN venues.location IS 'Расположение зала';
COMMENT ON COLUMN venues.capacity IS 'Вместимость зала';
COMMENT ON COLUMN venues.description IS 'Дополнительное описание зала';
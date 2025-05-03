-- Таблица заказов (покупка билетов)
CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id INT REFERENCES users(id) ON DELETE CASCADE,
                        event_id INT REFERENCES events(id) ON DELETE CASCADE,
                        quantity INT NOT NULL CHECK (quantity > 0),
                        total_price NUMERIC(10,2) NOT NULL,
                        currency VARCHAR(10) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT now(),
                        updated_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE orders IS 'Таблица заказов на билеты';
COMMENT ON COLUMN orders.user_id IS 'Пользователь, сделавший заказ (FK -> users)';
COMMENT ON COLUMN orders.event_id IS 'Мероприятие, на которое заказаны билеты (FK -> events)';
COMMENT ON COLUMN orders.quantity IS 'Количество билетов в заказе';
COMMENT ON COLUMN orders.total_price IS 'Общая сумма заказа';
COMMENT ON COLUMN orders.currency IS 'Валюта оплаты';
COMMENT ON COLUMN orders.status IS 'Статус заказа (напр., оплачено, отменено)';
COMMENT ON COLUMN orders.created_at IS 'Дата создания заказа';
COMMENT ON COLUMN orders.updated_at IS 'Дата последнего обновления заказа';

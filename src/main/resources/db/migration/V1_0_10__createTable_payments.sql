-- Таблица платежей
CREATE TABLE payments (
                          id SERIAL PRIMARY KEY,
                          order_id INT REFERENCES orders(id) ON DELETE CASCADE,
                          user_id INT REFERENCES users(id) ON DELETE CASCADE,
                          amount NUMERIC(10,2) NOT NULL,
                          currency VARCHAR(10) NOT NULL,
                          payment_method VARCHAR(50),
                          status VARCHAR(50) NOT NULL,
                          transaction_reference VARCHAR(255),
                          paid_at TIMESTAMP NOT NULL DEFAULT now()
);
COMMENT ON TABLE payments IS 'Таблица записей об оплате заказов';
COMMENT ON COLUMN payments.order_id IS 'Связь с заказом (FK -> orders)';
COMMENT ON COLUMN payments.user_id IS 'Пользователь, совершивший оплату (FK -> users)';
COMMENT ON COLUMN payments.amount IS 'Сумма оплаты';
COMMENT ON COLUMN payments.currency IS 'Валюта оплаты';
COMMENT ON COLUMN payments.payment_method IS 'Метод оплаты';
COMMENT ON COLUMN payments.status IS 'Статус платежа (напр., успешно, отклонен)';
COMMENT ON COLUMN payments.transaction_reference IS 'Идентификатор транзакции платежа';
COMMENT ON COLUMN payments.paid_at IS 'Дата и время оплаты';

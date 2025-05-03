CREATE TABLE organizer_requests
(
    id          SERIAL PRIMARY KEY,
    user_id     INT REFERENCES users (id) ON DELETE CASCADE,
    club_id     INT REFERENCES clubs (id) ON DELETE CASCADE,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    reviewed_at TIMESTAMP
);

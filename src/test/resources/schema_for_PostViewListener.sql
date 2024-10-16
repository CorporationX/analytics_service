CREATE TABLE analytics_event
(
    id SERIAL PRIMARY KEY,
    receiver_id BIGINT,
    actor_id    BIGINT,
    event_type  VARCHAR(64),
    received_at TIMESTAMP
);
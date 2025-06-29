CREATE TABLE IF NOT EXISTS analytics_premium_bought_event (
    id bigint PRIMARY KEY,
    payment_amount numeric(19, 2),
    subscription_duration integer,
    sent_at timestamptz,
    CONSTRAINT fk_premium_bought_to_analytics_event
        FOREIGN KEY(id)
        REFERENCES analytics_event(id)
        ON DELETE CASCADE
);

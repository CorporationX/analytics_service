CREATE TABLE IF NOT EXISTS premium_analytics (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    currency VARCHAR(30) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    country VARCHAR(30) NOT NULL,
    amount NUMERIC(19, 4) NOT NULL,
    premium_status VARCHAR(20) NOT NULL,
    premium_type VARCHAR(20) NOT NULL
);
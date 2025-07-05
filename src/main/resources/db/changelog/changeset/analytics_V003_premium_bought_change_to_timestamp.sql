ALTER TABLE analytics_premium_bought_event
ALTER COLUMN sent_at TYPE timestamp
USING sent_at AT TIME ZONE 'UTC';

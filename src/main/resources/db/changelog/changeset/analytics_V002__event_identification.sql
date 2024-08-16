DELETE FROM analytics_event;

ALTER TABLE analytics_event ADD COLUMN event_id UUID NOT NULL;
CREATE TABLE analytics_space.user_rating
(
    id                  UUID PRIMARY KEY,
    username            text,
    user_id             bigint,
    score               bigint,
    score_with_penalty  bigint,
    last_active_time    timestamp
);

CREATE TABLE analytics_space.analytics_event
(
    id          UUID PRIMARY KEY,
    receiver_id bigint,
    actor_id    bigint,
    event_type  text,
    received_at timestamp
);

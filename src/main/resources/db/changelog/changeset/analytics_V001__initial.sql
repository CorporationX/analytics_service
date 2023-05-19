CREATE TABLE user_profile_analytics (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    viewer_id bigint NOT NULL,
    viewed_at timestamptz DEFAULT current_timestamp
);
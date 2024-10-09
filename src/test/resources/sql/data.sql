ALTER SEQUENCE analytics_event_id_seq RESTART WITH 1;
INSERT INTO analytics_event(receiver_id, actor_id, event_type, received_at)
VALUES (1, 2, 'PROFILE_VIEW', '2024-06-01T15:00:00.0000'),
       (1, 3, 'PROFILE_VIEW', '2024-07-01T14:00:00.0000'),
       (2, 1, 'PROFILE_VIEW', NOW() - INTERVAL '1 day');

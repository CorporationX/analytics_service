INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (1, 2, 'USER_FOLLOWER', '2024-01-01 00:01:00');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (1, 3, 'USER_FOLLOWER', '2024-01-02 00:00:00');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (1, 4, 'USER_FOLLOWER', '2024-01-03 00:00:00');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (4, 1, 'PROJECT_FOLLOWER', '2024-01-04 00:00:00');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (1, 10, 'POST_LIKE', NOW() - INTERVAL '1 HOUR');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (1, 2, 'POST_LIKE', NOW() - INTERVAL '25 HOUR');
INSERT INTO analytics_event (receiver_id, actor_id, event_type, received_at) VALUES (2, 3, 'USER_FOLLOWER', '2024-01-07 00:00:00');
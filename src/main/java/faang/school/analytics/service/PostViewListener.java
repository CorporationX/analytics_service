package faang.school.analytics.service;

import faang.school.analytics.dto.event.PostViewEvent;
import faang.school.analytics.mapper.PostViewEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostViewListener {
    private final AnalyticsEventService analyticsEventService;
    private final PostViewEventMapper mapper;

    @KafkaListener(topics = "${user-post-viewed.topic-name}", groupId = "notification-group",
                    containerFactory = "postViewEventConcurrentKafkaFactory")
    public void listen(PostViewEvent dto) {
        analyticsEventService.saveViewPostEvent(mapper.toEntity(dto));
    }
}
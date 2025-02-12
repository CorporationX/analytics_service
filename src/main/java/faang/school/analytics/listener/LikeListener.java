package faang.school.analytics.listener;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.mapper.LikeEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LikeListener {

    private final LikeEventMapper likeEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "likes", groupId = "group1",
            containerFactory = "eventKafkaListenerContainerFactory")
    void listener(LikeEvent event) {
        analyticsEventService.addEvent(likeEventMapper.toAnalyticsEvent(event));
    }
}

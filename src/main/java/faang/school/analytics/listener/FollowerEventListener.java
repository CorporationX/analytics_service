package faang.school.analytics.listener;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.follower}",
            properties = "spring.json.value.default.type:faang.school.analytics.dto.FollowerEvent"
    )
    public void listen(FollowerEvent followerEvent) {
        log.info("Follower event received: {}", followerEvent);
        analyticsEventService.saveEvent(analyticsEventMapper.toDto(followerEvent));
        log.info("Follower event saved to database");
    }
}
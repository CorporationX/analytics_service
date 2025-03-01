package faang.school.analytics.listener;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topics.follower}", containerFactory = "followerListener")
public class FollowerEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaHandler
    public void listen(FollowerEvent followerEvent) {
        log.info("Follower event received: {}", followerEvent);
        analyticsEventService.saveEvent(analyticsEventMapper.toDto(followerEvent));
        log.info("Follower event saved to database");
    }
}
package faang.school.analytics.listener;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileViewEventListener {

    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${user-profile-viewed.topic-name}", groupId = "notification-group",
                    containerFactory = "profileViewEventConcurrentKafkaFactory")
    public void listen(ProfileViewEventDto eventDto) {

        analyticsEventService.addEvent(eventMapper.toAnalyticsEvent(eventDto));
        log.info("Received Message in group notification-group: " + eventDto);
    }
}

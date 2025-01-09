package faang.school.analytics.service;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final EventMapper eventMapper;

    public void saveFollowerEvent(FollowerEvent followerEvent) {
        AnalyticsEvent event = eventMapper.toAnalyticsEvent(followerEvent);
        event.setEventType(EventType.FOLLOWER);

        analyticsEventRepository.save(event);
        log.info("Save follower event {}", followerEvent);
    }


}

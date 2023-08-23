package faang.school.analytics.service;

import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.event.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeEventService {
    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveMessage(String message) {
        LikeEvent event = LikeEvent.fromString(message);
        AnalyticsEvent newEvent = eventMapper.toAnalyticsEvent(event);
        eventMapper.setLikeType(newEvent);
        analyticsEventRepository.save(newEvent);
    }
}
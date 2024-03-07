package faang.school.analytics.service;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveRecommendationEvent(RecommendationEvent recommendationEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(recommendationEvent);
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventRepository.save(analyticsEvent);
        log.info(recommendationEvent + " is saved to DB");
    }

    public void saveFollowerEvent(FollowerEventDto followerEventDto) {
        AnalyticsEvent followerEvent = analyticsEventMapper.toEntity(followerEventDto);
        followerEvent.setEventType(EventType.FOLLOWER);
        log.info("Старт saveAnalyticsEvent: {}", followerEventDto);
        analyticsEventRepository.save(followerEvent);
        log.info("Сохранен AnalyticsEvent - {} ", followerEvent);
    }
}

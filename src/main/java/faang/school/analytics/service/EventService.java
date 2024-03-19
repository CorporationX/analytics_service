package faang.school.analytics.service;

import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.dto.event_broker.FollowerEvent;
import faang.school.analytics.dto.event_broker.PremiumEvent;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final EventMapper eventMapper;

    @Async("taskExecutor")
    public void savePremium(PremiumEvent premiumEvent) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(premiumEvent);
        analyticsEventRepository.save(analyticsEvent);
    }

    @Async("taskExecutor")
    public void saveFollower(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(followerEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}

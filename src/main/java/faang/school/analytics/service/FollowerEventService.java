package faang.school.analytics.service;

import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowerEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveFollowerEvent(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(followerEvent);

        analyticsEvent.setReceivedAt(followerEvent.getSubscriptionTime());

        analyticsEventRepository.save(analyticsEvent);
    }
}
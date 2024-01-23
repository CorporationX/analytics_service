package faang.school.analytics.service;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.FollowerEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final FollowerEventMapper followerEventMapper;
    public void save(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = followerEventMapper.toAnalyticsEvent(followerEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}

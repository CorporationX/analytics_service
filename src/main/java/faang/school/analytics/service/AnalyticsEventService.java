package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticEventMapper;
import faang.school.analytics.messaging.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticEventMapper analyticEventMapper;

    public void saveAnalyticsEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    public void saveProfileViewEvent(ProfileViewEvent profileViewEvent) {
        AnalyticsEvent event = analyticEventMapper.profileViewToAnalyticsEvent(profileViewEvent);
        event.setEventType(EventType.PROFILE_VIEW);
        saveAnalyticsEvent(event);
    }
}

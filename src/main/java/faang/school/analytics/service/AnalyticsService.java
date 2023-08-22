package faang.school.analytics.service;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEvent profileViewEventSave(ProfileViewEvent profileViewEvent) {
        AnalyticsEvent analyticsEvent =
                analyticsEventMapper.fromProfileViewEventToAnalyticsEvent(profileViewEvent);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);

       return analyticsEventRepository.save(analyticsEvent);
    }
}

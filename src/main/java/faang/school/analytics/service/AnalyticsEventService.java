package faang.school.analytics.service;

import faang.school.analytics.message.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void saveProfileView(ProfileViewEvent profileViewEvent) {
        log.info("Trying to save profileViewEvent: {} to database", profileViewEvent);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(profileViewEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}

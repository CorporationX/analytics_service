package faang.school.analytics.service;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void saveAnalyticsEvent(AnalyticsEvent analyticsvent) {
        analyticsEventRepository.save(analyticsvent);
    }
}

package faang.school.analytics.service;

import faang.school.analytics.dto.redis.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics() {
        return null;
    }
}

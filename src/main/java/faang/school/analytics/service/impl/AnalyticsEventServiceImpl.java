package faang.school.analytics.service.impl;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Event saved successfully: {}", event);
    }
}

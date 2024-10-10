package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnalyticsServiceImpl implements AnalyticsEventService {
    @Override
    public void saveEvent(AnalyticsEvent analyticsEvent) {
        log.info("Saving event: {}", analyticsEvent);
    }
}

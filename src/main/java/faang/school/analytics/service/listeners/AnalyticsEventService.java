package faang.school.analytics.service.listeners;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEventToDb(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save( analyticsEvent );
        log.info( "AnalyticsEvent saved to database!" );
    }
}

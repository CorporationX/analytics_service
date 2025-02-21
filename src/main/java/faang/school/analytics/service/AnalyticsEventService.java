package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService implements EventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void saveAnalyticsEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }
}

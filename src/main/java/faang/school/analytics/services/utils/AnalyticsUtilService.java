package faang.school.analytics.services.utils;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsUtilService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent saveEvent(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    public Stream<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }
}

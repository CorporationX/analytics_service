package faang.school.analytics.services.utils;

import faang.school.analytics.exceptions.ErrorMessage;
import faang.school.analytics.exceptions.NotFoundException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsUtilService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent saveEvent(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    public Stream<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType) {
       List<AnalyticsEvent> eventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType).toList();
        if (eventStream.isEmpty()) {
            log.info("No analytic event: {} found for receiverId: {}",eventType, receiverId);
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
        return eventStream.stream();
    }
}

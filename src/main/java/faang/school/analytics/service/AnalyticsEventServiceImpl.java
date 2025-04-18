package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository eventRepository;

    @Override
    public List<AnalyticsEvent> getAnalytics(Long receiverId, EventType eventType, LocalDateTime start, LocalDateTime end) {
        log.info("Fetching analytics for receiverId={}, eventType={}, start={}, end={}",
                receiverId, eventType, start, end);
        return eventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> !event.getReceivedAt().isBefore(start) && !event.getReceivedAt().isAfter(end))
                .toList();
    }
}

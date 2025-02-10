package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Analytic event save.");
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId,
                eventType);

        return null;
    }
}

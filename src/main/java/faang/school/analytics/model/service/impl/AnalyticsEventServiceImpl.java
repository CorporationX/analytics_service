package faang.school.analytics.model.service.impl;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.model.service.AnalyticsEventService;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved event: {}", event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(Long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        LocalDateTime startDate;
        if (interval != null) {
            startDate = Interval.getFromDate(interval);
        } else {
            startDate = from;
        }
        List<AnalyticsEvent> filteredEvents = events.filter(event ->
                        event.getReceivedAt().isAfter(startDate) && event.getReceivedAt().isBefore(to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .toList();
        List<AnalyticsEventDto> result = filteredEvents.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        log.info("Returning {} analytics event(s)", result.size());
        return result;
    }
}
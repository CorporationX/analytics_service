package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public void save(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved event: {}", event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {

        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            LocalDateTime fromDate = Interval.getFromDate(interval);
            return analyticsEvents
                    .filter(event -> event.getReceivedAt().isAfter(fromDate))
                    .map(analyticsEventMapper::toDto)
                    .toList();
        }

        return analyticsEvents
                .filter(event -> event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}

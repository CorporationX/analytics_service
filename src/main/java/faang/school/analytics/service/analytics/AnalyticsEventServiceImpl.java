package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.model.Interval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        AnalyticsEvent saved = analyticsRepository.save(event);
        log.info("event has been saved to DB, event id = {}", saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(
            long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        AnalyticsInterval analyticsInterval = setAnalyticsInterval(interval, from, to);
        List<AnalyticsEvent> analyticsEventList = analyticsRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event ->
                        event.getReceivedAt().isAfter(analyticsInterval.startDate)
                                && event.getReceivedAt().isBefore(analyticsInterval.endDate))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();

        return analyticsEventList.stream()
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private record AnalyticsInterval(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
    }

    private AnalyticsInterval setAnalyticsInterval(Interval interval, LocalDateTime from, LocalDateTime to) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        if (interval != null) {
            endDate = LocalDateTime.now();
            startDate = interval.getStartDate();
        } else {
            startDate = from;
            endDate = to;
        }
        return new AnalyticsInterval(startDate, endDate);
    }
}
package faang.school.analytics.service.impl;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEventDto event) {
        analyticsEventRepository.save(analyticsEventMapper.toEntity(event));
        log.info("Analytic event saved.");
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventRequestDto analyticsEventRequestDto) {
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository
                .findByReceiverIdAndEventType(analyticsEventRequestDto.getReceiverId(),
                        analyticsEventRequestDto.getEventType());
        List<AnalyticsEvent> analyticsEvents = analyticsEventStream
                .filter(event -> checkDate(event, analyticsEventRequestDto.getFrom(), analyticsEventRequestDto.getTo(),
                        analyticsEventRequestDto.getInterval()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
        log.info("Founded {} record's", analyticsEvents.size());
        return analyticsEventMapper.toDto(analyticsEvents);
    }

    private boolean checkDate(AnalyticsEvent event, LocalDateTime from, LocalDateTime to, Interval interval) {
        LocalDateTime receivedAt = event.getReceivedAt();
        if (receivedAt == null) {
            return false;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (interval != null) {
            return interval.getStartDate(localDateTime).isBefore(receivedAt) && localDateTime.isAfter(receivedAt);
        } else {
            return from.isBefore(receivedAt) && to.isAfter(receivedAt);
        }
    }
}

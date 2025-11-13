package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.exception.AnalyticsValidationException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.debug("Analytics event saved - receiver: {}, type: {}",
                event.getReceiverId(), event.getEventType());
    }

    @Override
    public void saveEvent(Object eventDto) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        analyticsEventRepository.save(event);
        log.debug("Analytics event saved from DTO - type: {}", eventDto.getClass().getSimpleName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                        LocalDateTime from, LocalDateTime to) {

        validateAnalyticsParameters(interval, from, to);
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> isEventInRange(event, interval, from, to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private boolean isEventInRange(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        if (interval != null) {
            startDate = interval.getStart();
            endDate = interval.getEnd();
        } else {
            startDate = from;
            endDate = to;
        }
        return !event.getReceivedAt().isBefore(startDate) &&
                !event.getReceivedAt().isAfter(endDate);
    }

    private void validateAnalyticsParameters(Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new AnalyticsValidationException("Specify interval or both start and end dates");
        }
        if (from != null && to != null && from.isAfter(to)) {
            throw new AnalyticsValidationException("From date must be before to date");
        }
    }
}
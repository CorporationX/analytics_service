package faang.school.analytics.service.analytics_event;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.analytics_event.AnalyticsEventGetDto;
import faang.school.analytics.exception.IntervalsNotValidException;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserContext userContext;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent) {
        log.debug("Saving event in DB with id {} and event type {}", analyticsEvent.getId(),
                analyticsEvent.getEventType());
        AnalyticsEventDto savedDto = analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsEvent));
        log.debug("Successfully saved event!");
        return savedDto;
    }

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventGetDto analyticsEventGetDto) {
        long receiverId = userContext.getUserId();
        EventType eventType = analyticsEventGetDto.getEventType();

        if (analyticsEventGetDto.getInterval() != null && !analyticsEventGetDto.getInterval().isBlank()) {
            LocalDateTime intervalTime = Interval.startDate(analyticsEventGetDto.getInterval());
            log.debug("Getting all events from {} to present moment", intervalTime);
            return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                    .filter(event -> event.getReceivedAt().isAfter(intervalTime))
                    .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                    .map(analyticsEventMapper::toDto)
                    .toList();
        } else if (analyticsEventGetDto.getFrom() != null && analyticsEventGetDto.getTo() != null) {
            LocalDateTime from = analyticsEventGetDto.getFrom();
            LocalDateTime to = analyticsEventGetDto.getTo();
            log.debug("Getting all events in a period from {} to {}", from, to);
            return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                    .filter(event -> event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to))
                    .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                    .map(analyticsEventMapper::toDto)
                    .toList();
        } else {
            log.error("Interval or from and to dates are null!");
            throw new IntervalsNotValidException("Interval or from and to dates are null! Please check your request!");
        }
    }
}

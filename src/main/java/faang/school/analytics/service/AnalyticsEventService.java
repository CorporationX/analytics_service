package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.exception.MapperReadValueException;
import faang.school.analytics.filter.AnalyticsEventFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static faang.school.analytics.model.Interval.getDaysByInterval;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsEventFilter> analyticsEventFilters;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticEventDto> getAnalytics(AnalyticInfoDto analyticInfoDto) {

        long receiverId = analyticInfoDto.getReceiverId();
        EventType eventType = analyticInfoDto.getEventType();
        Interval interval = analyticInfoDto.getInterval();
        LocalDateTime from = analyticInfoDto.getFrom();
        LocalDateTime to = analyticInfoDto.getTo();

        List<AnalyticsEvent> analyticsEvents = interval == null ?
                analyticsEventRepository.getBetweenDate(from, to) :
                analyticsEventRepository.getByDays(getCurrentDateTimeMinusIntervalDays(interval));

        return analyticsEvents.stream()
                .filter(analyticsEvent -> analyticsEvent.getEventType().equals(eventType))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private LocalDateTime getCurrentDateTimeMinusIntervalDays(Interval interval) {
        int daysByInterval = getDaysByInterval(interval);
        return LocalDateTime.now().minusDays(daysByInterval);
    }


    @Transactional
    public void saveLikeAnalytics(Message message){
        AnalyticsEvent analyticsEvent = getEventType(message, LikeEvent.class, analyticsEventMapper::toAnalyticsEventFromLikeEvent);
        if (analyticsEvent != null) {
            analyticsEventRepository.save(analyticsEvent);
        }
    }

    private <T> AnalyticsEvent getEventType(Message message, Class<T> eventType, Function<T, AnalyticsEvent> mapper) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            return mapper.apply(event);
        } catch (IOException e) {
            log.error("Error readValue: {}", eventType, e);
            throw new MapperReadValueException(e.getMessage());
        }
    }
}
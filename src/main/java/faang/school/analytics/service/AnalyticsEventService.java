package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@RequiredArgsConstructor
@Builder
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsEventFilter> analyticsEventFilters;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filterDto) {
        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository
                .findByReceiverIdAndEventTypeOrderByReceiverIdDesc(filterDto.getReceiverId(), filterDto.getEventType());

        for (AnalyticsEventFilter analyticsEventFilter : analyticsEventFilters) {
            analyticsEvents = analyticsEventFilter.filter(analyticsEvents, filterDto);
        }

        return analyticsEvents.map(analyticsEventMapper::toDto).toList();
    }


//
//    @Transactional
//    public void saveLikeAnalytics(Message message){
//        AnalyticsEvent analyticsEvent = getEventType(message, LikeEvent.class, analyticsEventMapper::toAnalyticsEventFromLikeEvent);
//        if (analyticsEvent != null) {
//            analyticsEventRepository.save(analyticsEvent);
//        }
//    }
//
//    private <T> AnalyticsEvent getEventType(Message message, Class<T> eventType, Function<T, AnalyticsEvent> mapper) {
//        try {
//            T event = objectMapper.readValue(message.getBody(), eventType);
//            return mapper.apply(event);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//    }
}
package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

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
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
package faang.school.analytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.event.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeEventService {
    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final ObjectMapper objectMapper;
    public void saveMessage(String message) throws JsonProcessingException {
        //LikeEvent event = LikeEvent.fromString(message);
        LikeEvent event = objectMapper.readValue(message, LikeEvent.class);
        AnalyticsEvent newEvent = eventMapper.toAnalyticsEvent(event);
        eventMapper.setLikeType(newEvent);
        analyticsEventRepository.save(newEvent);
    }
}
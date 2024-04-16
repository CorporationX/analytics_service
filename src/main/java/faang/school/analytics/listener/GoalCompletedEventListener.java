package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEventDto goalCompletedEventDto = objectMapper.readValue(message.getBody(), GoalCompletedEventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(goalCompletedEventDto);
            analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
            analyticsEventService.save(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent goalCompletedEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            AnalyticsEventResponseDto analyticsEventResponseDto = buildAnalyticsEventResponseDto(goalCompletedEvent);
            AnalyticsEvent event = analyticsEventMapper.toEntity(analyticsEventResponseDto);
            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            log.error("Exception occurred while parsing the message", e);
            throw new MessageProcessingException("Failed to process message: " + message, e);
        }
    }

    private AnalyticsEventResponseDto buildAnalyticsEventResponseDto(GoalCompletedEvent goalCompletedEvent) {
        return AnalyticsEventResponseDto.builder()
                .receiverId(goalCompletedEvent.goalId())
                .actorId(goalCompletedEvent.userId())
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(LocalDateTime.now())
                .build();
    }
}

package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.GoalEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("goalEventListener")
@Slf4j
public class GoalEventListener extends AbstractEventListener<GoalEventDto> implements MessageListener {

    public GoalEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalEventDto goalEventDto = objectMapper.readValue(message.getBody(), GoalEventDto.class);
            sendAnalytics(goalEventDto);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.GOAL_COMPLETED;
    }
}

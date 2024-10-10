package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.GoalCompletedEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEventDto> implements MessageListener {

    public GoalCompletedEventListener(AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper,
                                      ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEventDto dto = handleEvent(message, GoalCompletedEventDto.class);
        AnalyticsEvent entity = analyticsEventMapper.toEntity(dto);
        entity.setEventType(EventType.GOAL_COMPLETED);
        analyticsEventService.saveEvent(entity);
    }
}

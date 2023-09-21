package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {

    @Autowired
    public CommentEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto event = convertJsonToString(message, CommentEventDto.class);
        AnalyticsEvent analyticsEvent = super.analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        super.analyticsEventService.create(analyticsEvent);
    }
}

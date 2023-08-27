package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final JsonObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = objectMapper.fromJson(message.getBody(), CommentEventDto.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(commentEventDto);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventService.save(analyticsEvent);
    }
}
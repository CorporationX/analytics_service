package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.JsonObjectMapper;
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
public class CommentMessageListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final JsonObjectMapper jsonObjectMapper;
    private  final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = jsonObjectMapper.readValue(message.getBody(), CommentEventDto.class);
        EventDto analyticsEvent = analyticsEventMapper.toEventDto(commentEventDto);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Message from topic: {}, has been received.", message.getChannel());
    }
}
package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMessageListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final JsonObjectMapper jsonObjectMapper;
    private  final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = jsonObjectMapper.readValue(message.getBody(), CommentEventDto.class);
        AnalyticsEventDto analyticsEvent = analyticsEventMapper.toAnalyticsEventDto(commentEventDto);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}

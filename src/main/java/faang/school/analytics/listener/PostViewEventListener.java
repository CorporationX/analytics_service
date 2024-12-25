package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.exception.AnalyticsServiceException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEvent postViewEvent = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            EventType eventType = EventType.POST_VIEW;

            AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto(
                    0L,
                    postViewEvent.getAuthor_id(),
                    postViewEvent.getUser_id(),
                    eventType.name(),
                    postViewEvent.getTimeStamp().toString()
            );
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
            analyticsEventService.saveEvent(analyticsEvent);

        } catch (IOException e) {
            throw new AnalyticsServiceException("Incorrect format of PostViewEvent");
        }
    }
}

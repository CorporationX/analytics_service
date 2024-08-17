package faang.school.analytics.redis.lisener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.events.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEvent postViewEvent = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            AnalyticsEventDto dto = analyticsEventMapper.toDto(postViewEvent);
            dto.setEventType(EventType.POST_VIEW);
            analyticsEventService.saveEvent(analyticsEventMapper.toEntity(dto));
            log.info("post view event {} was saved successfully", postViewEvent.getPostId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


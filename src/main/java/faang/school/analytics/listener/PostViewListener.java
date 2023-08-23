package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.PostViewEventDto;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEventDto postViewEventDto = objectMapper.readValue(message.getBody(), PostViewEventDto.class);
            log.info("Received message: {}", postViewEventDto);
            AnalyticsEvent event = analyticsEventMapper.toEntity(postViewEventDto);
            event.setEventType(EventType.POST_VIEW);

            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsCommentEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsCommentEventMapper commentEventMapper;
    private final AnalyticsEventService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEventDto commentEventDto = objectMapper.readValue(message.getBody(), CommentEventDto.class);
            AnalyticsEvent analyticsEvent = commentEventMapper.toAnalyticsEvent(commentEventDto);
            AnalyticsEvent savedAnalyticsEvent = analyticsService.saveEvent(analyticsEvent);
            log.info("Received new analytics data: {}", savedAnalyticsEvent);

        } catch (IOException e) {
            log.error("Error with mapping to CommentEventDto");
            throw new IllegalArgumentException("Error with mapping to CommentEventDto");
        }

    }
}

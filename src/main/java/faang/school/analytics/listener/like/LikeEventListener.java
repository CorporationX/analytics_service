package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEventDto;
import faang.school.analytics.mapper.event.LikeEventMapper;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
@Slf4j
public class LikeEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final LikeEventMapper likeEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String jsonString = new String(message.getBody(), StandardCharsets.UTF_8).trim();
        log.debug("Received new message from like-events channel");
        jsonString = jsonString.replaceFirst("^[^\\[{]*(\\{.*?}.*)$", "$1");
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(jsonString, LikeEventDto.class);
            log.debug("Saving new analytic report with event type of - {} from channel: {} time received: {}",
                    likeEventDto.getEventType(),
                    Arrays.toString(message.getChannel()),
                    LocalDate.now());
            analyticsEventService.saveEvent(likeEventMapper.fromLikeEventDtoToEntity(likeEventDto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

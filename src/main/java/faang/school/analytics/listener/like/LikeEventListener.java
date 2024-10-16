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
import java.time.LocalDateTime;
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
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(),
                    LikeEventDto.class);
            log.debug("Saving new analytic report with event type of - {} from channel: {} time received: {}",
                    likeEventDto.getEventType(),
                    Arrays.toString(message.getChannel()),
                    LocalDateTime.now());
            log.debug("Saving to analytics DB");
            analyticsEventService.saveEvent(likeEventMapper.fromLikeEventDtoToEntity(likeEventDto));
            log.debug("Saved successfully!");
        } catch (IOException e) {
            log.error("Something went wrong while reading {}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }
}

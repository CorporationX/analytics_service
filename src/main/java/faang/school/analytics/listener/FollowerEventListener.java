package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.FollowerEventDto;
import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.mapper.FollowerEventMapper;
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
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final FollowerEventMapper followerEventMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
            log.info("Received message: {}", followerEventDto);
            AnalyticsEvent event = followerEventMapper.toEntity(followerEventDto);

            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            log.error("event could not be received, error message: {}", e.getMessage());
        }

    }
}

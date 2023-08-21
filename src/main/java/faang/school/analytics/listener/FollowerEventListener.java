package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
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

    private final AnalyticsEventService analyticsEventService;
    private final FollowerEventMapper followerEventMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto incomingDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
            AnalyticsEvent outgoingDto = followerEventMapper.toModel(incomingDto);
            outgoingDto.setEventType(EventType.FOLLOWER);
            analyticsEventService.saveEvent(outgoingDto);
        } catch (IOException exception) {
            exception.printStackTrace();
            log.error("Error when deserializing JSON to object");
        }
        log.info("Message from topic: {}, has been received.", message.getChannel());
    }
}
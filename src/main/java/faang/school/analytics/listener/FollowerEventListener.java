package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.FollowerEventMapper;
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
public class FollowerEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final FollowerEventMapper followerEventMapper;
    private final JsonObjectMapper jsonObjectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEventDto incomingDto = jsonObjectMapper.readValue(message.getBody(), FollowerEventDto.class);
        AnalyticsEventDto outgoingDto = followerEventMapper.toDto(incomingDto);
        outgoingDto.setEventType(EventType.FOLLOWER);
        analyticsEventService.saveEvent(outgoingDto);

        log.info("Message from topic: {}, has been received.", message.getChannel());
    }
}
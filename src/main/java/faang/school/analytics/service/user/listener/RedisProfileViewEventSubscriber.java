package faang.school.analytics.service.user.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisProfileViewEventSubscriber implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from \"profileViewEvent\" topic");
        try {
            List<ProfileViewEventDto> profileViewEventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProfileViewEventDto.class));

            analyticsEventService.saveAllEvents(analyticsEventMapper.toAnalyticsEvents(profileViewEventDtoList));
            log.info("{} user view events saved", profileViewEventDtoList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

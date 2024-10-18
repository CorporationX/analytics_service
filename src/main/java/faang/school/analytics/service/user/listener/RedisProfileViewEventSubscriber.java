package faang.school.analytics.service.user.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.redis.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class RedisProfileViewEventSubscriber extends AbstractEventListener<ProfileViewEventDto> {
    private final ObjectMapper objectMapper;
    private final Topic topic;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public RedisProfileViewEventSubscriber(ObjectMapper javaTimeModuleObjectMapper,
                                           Topic profileViewEventTopic,
                                           AnalyticsEventService analyticsEventService,
                                           AnalyticsEventMapper analyticsEventMapper) {
        super(javaTimeModuleObjectMapper, profileViewEventTopic);
        this.objectMapper = javaTimeModuleObjectMapper;
        this.topic = profileViewEventTopic;
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void saveEvent(ProfileViewEventDto event) {
    }

    @Override
    public Class<ProfileViewEventDto> getEventType() {
        return null;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from \"{}}\" topic", topic.getTopic());
        try {
            List<ProfileViewEventDto> profileViewEventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProfileViewEventDto.class));

            analyticsEventService.saveAllEvents(analyticsEventMapper.profileViewToAnalyticsEvents(profileViewEventDtoList));
            log.info("{} user view events saved", profileViewEventDtoList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

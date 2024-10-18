package faang.school.analytics.service.user.premium.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
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
public class RedisPremiumBoughtEventSubscriber extends AbstractEventListener<PremiumBoughtEventDto> {
    private final ObjectMapper objectMapper;
    private final Topic topic;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public RedisPremiumBoughtEventSubscriber(ObjectMapper javaTimeModuleObjectMapper,
                                             Topic premiumBoughtEventTopic,
                                             AnalyticsEventService analyticsEventService,
                                             AnalyticsEventMapper analyticsEventMapper) {
        super(javaTimeModuleObjectMapper, premiumBoughtEventTopic);
        this.objectMapper = javaTimeModuleObjectMapper;
        this.topic = premiumBoughtEventTopic;
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void saveEvent(PremiumBoughtEventDto event) {
    }

    @Override
    public Class<PremiumBoughtEventDto> getEventType() {
        return null;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from \"{}}\" topic", topic.getTopic());
        try {
            List<PremiumBoughtEventDto> premiumBoughtEventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, PremiumBoughtEventDto.class));

            analyticsEventService.saveAllEvents(analyticsEventMapper.premiumBoughtToAnalyticsEvents(premiumBoughtEventDtoList));
            log.info("{} user premium bought events saved", premiumBoughtEventDtoList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package faang.school.analytics.service.user.premium.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPremiumBoughtEventSubscriber implements MessageListener {
    @Value("${app.user-premium-redis-config.premium_bought_event_topic}")
    private String premiumBoughtEventTopic;

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from \"{}}\" topic", premiumBoughtEventTopic);
        try {
            List<PremiumBoughtEventDto> premiumBoughtEventDtoList = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, PremiumBoughtEventDto.class));

            analyticsEventService.saveAllEvents(analyticsEventMapper.toAnalyticsEvents(premiumBoughtEventDtoList));
            log.info("{} user premium bought events saved", premiumBoughtEventDtoList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

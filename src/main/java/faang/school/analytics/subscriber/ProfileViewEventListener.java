package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.ProfileViewMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final ProfileViewMapper profileViewMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfileViewEvent profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            log.info("Получил ProfileViewEvent");
            AnalyticsEvent analyticsEvent = profileViewMapper.toAnalyticsEvent(profileViewEvent);
            profileViewMapper.setEventType(analyticsEvent);
            log.info("Маппинг в analyticsEvent прошел успешно");
            analyticsEventService.saveEventAnalytics(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

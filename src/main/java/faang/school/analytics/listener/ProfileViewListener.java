package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.ProfileViewEvent;
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
public class ProfileViewListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonString = new String(message.getBody());
            ProfileViewEvent profileView = objectMapper.readValue(jsonString, ProfileViewEvent.class);

            AnalyticsEventDTO eventDto = AnalyticsEventDTO.builder()
                    .eventType(EventType.PROFILE_VIEW)
                    .actorId(profileView.getViewId())
                    .receiverId(profileView.getProfileId())
                    .receivedAt(profileView.getTimestamp())
                    .build();

            log.info("Received profile view event: {} ", profileView);
            analyticsEventService.saveEvent(eventDto);
        } catch (IOException e) {
            log.error("Error processing profile view message {} ", new String(message.getBody()), e);
        }
    }
}

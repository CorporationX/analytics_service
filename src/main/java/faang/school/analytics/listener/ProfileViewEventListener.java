package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.exception.AnalyticsConvertingException;
import faang.school.analytics.mapper.ProfileViewEventMapper;
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
public class ProfileViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final ProfileViewEventMapper profileViewEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.debug("Received new Profile completed event: {}", message.getBody());
            ProfileViewEvent profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            AnalyticsEventDto analyticsEventDto = profileViewEventMapper.toAnalyticsDto(profileViewEvent);
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (IOException e) {
            throw new AnalyticsConvertingException("Failed to deserialize message", e);
        }

    }
}

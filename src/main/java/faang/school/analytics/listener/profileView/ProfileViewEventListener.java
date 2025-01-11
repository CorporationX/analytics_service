package faang.school.analytics.listener.profileView;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.profileView.ProfileViewEvent;
import faang.school.analytics.exception.ProfileViewEventException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(ProfileViewEventListener.class);
    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            log.info("Received event: {}", event);
            AnalyticsEvent analyticsEvent = mapper.toEntity(event);
            log.info("Saved event to database: {}", analyticsEvent);
            service.saveEvent(analyticsEvent);
        } catch (IOException e) {
            String messageBody = new String(message.getBody());
            log.error("Failed to parse event: {}", messageBody, e);
            throw new ProfileViewEventException("Error parsing FundRaisedEvent");
        }
    }

}

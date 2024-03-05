package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Alexander Bulgakov
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SearchAppearanceEventListener implements MessageListener {
    private final ApplicationEventPublisher eventPublisher;

    private final ObjectMapper objectMapper;


    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            SearchAppearanceEvent event = objectMapper
                    .readValue(messageBody, SearchAppearanceEvent.class);

            eventPublisher.publishEvent(event);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
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
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    private final ObjectMapper objectMapper;


    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            SearchAppearanceEvent event = objectMapper
                    .readValue(messageBody, SearchAppearanceEvent.class);

            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEventService.save(analyticsEvent);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

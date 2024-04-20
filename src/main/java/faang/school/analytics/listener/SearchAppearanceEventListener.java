package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.messagebroker.SearchAppearanceEvent;
import faang.school.analytics.mapper.SearchUserMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final SearchUserMapper searchUserMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SearchAppearanceEvent searchAppearanceEvent = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
            AnalyticsEvent analyticsEvent = searchUserMapper.toAnalyticEvent(searchAppearanceEvent);
            searchUserMapper.setEventType(analyticsEvent);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

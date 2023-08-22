package faang.school.analytics.listener;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {
    private final JsonObjectMapper jsonObjectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SearchAppearanceEventDto event = jsonObjectMapper.fromJson(message.getBody(), SearchAppearanceEventDto.class);
            event.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        analyticsEventService.save(event);
        System.out.println("Analytics event saved: " + event);
    }
}
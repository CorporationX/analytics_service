package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.SearchAppearance.InputSearchAppearanceEvent;
import faang.school.analytics.model.event.SearchAppearance.SearchAppearanceEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputSearchAppearanceEventListener extends AbstractEventListener<InputSearchAppearanceEvent> implements MessageListener {
    public InputSearchAppearanceEventListener(
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper,
            ObjectMapper objectMapper
    ) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        InputSearchAppearanceEvent inputEvent = handleEvent(message, InputSearchAppearanceEvent.class);

        List<AnalyticsEvent> analyticsEvents = inputEvent.foundUsersId().stream()
                .map(userId -> new SearchAppearanceEvent(inputEvent.requesterId(), userId, inputEvent.requestDateTime()))
                .map(event -> {
                    AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
                    analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
                    return analyticsEvent;
                })
                .toList();

        analyticsEvents.forEach(analyticsEventService::saveEvent);
    }
}

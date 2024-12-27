package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.message.event.ViewedUserEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ViewedUsersEventListener extends AbstractEventListener<ViewedUserEvent> implements MessageListener {

    private final AnalyticsEventMapper mapper;

    protected ViewedUsersEventListener(ObjectMapper objectMapper,
                                       AnalyticsEventService analyticsEventService,
                                       AnalyticsEventMapperImpl mapper) {
        super(objectMapper, analyticsEventService);
        this.mapper = mapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ViewedUserEvent.class, mapper::toAnalyticsEvent);
    }
}

package faang.school.analytics.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FollowerEventListener extends AbstractListener<FollowerEvent> {

    @Autowired
    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected FollowerEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), FollowerEvent.class);
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(FollowerEvent event) {
        return analyticsEventMapper.toEntity(event);
    }

}
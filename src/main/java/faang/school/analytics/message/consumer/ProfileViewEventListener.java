package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.message.event.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;

    @Autowired
    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, analyticsEventMapper::toAnalyticsEvent);
    }
}

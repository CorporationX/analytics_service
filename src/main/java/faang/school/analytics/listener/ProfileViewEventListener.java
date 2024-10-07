package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ProfileViewEventListener extends AbstractListener<ProfileViewEvent> {

    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected ProfileViewEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(ProfileViewEvent event) {
        return analyticsEventMapper.toAnalyticsEvent(event);
    }

}
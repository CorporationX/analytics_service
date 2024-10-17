package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.dto.ProfileViewEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractRedisListener<ProfileViewEvent> {
    private final AnalyticsEventMapper mapper;

    public ProfileViewEventListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper,
                                    AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
        this.mapper = mapper;
    }

    public void onMessage(Message message, byte[] pattern) {
        handleEvent(ProfileViewEvent.class, message, mapper::fromProfileViewToEntity);
    }
}

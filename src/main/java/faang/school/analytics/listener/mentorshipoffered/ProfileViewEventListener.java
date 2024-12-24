package faang.school.analytics.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.model.dto.ProfileViewEvent;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {


 private final AnalyticsEventService analyticsEventService;
 private final AnalyticsEventMapper analyticsEventMapper;

    public ProfileViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event
                -> analyticsEventService.saveEvent(analyticsEventMapper.toEntityFromProfileViewEvent(event)));
    }
}
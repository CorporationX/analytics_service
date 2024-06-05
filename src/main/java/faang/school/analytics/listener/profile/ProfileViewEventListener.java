package faang.school.analytics.listener.profile;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.profile.ProfileViewEventDto;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> implements MessageListener {

    public ProfileViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEventDto.class, analyticsEventService::saveProfileViewEvent);
    }
}
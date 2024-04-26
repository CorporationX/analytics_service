package faang.school.analytics.listeners;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.mappers.AbstractAnalyticsEventMapper;
import faang.school.analytics.mappers.JsonMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProfileViewEventListener extends AbstractListener<ProfileViewEventDto> implements MessageListener {

    public ProfileViewEventListener(List<AbstractAnalyticsEventMapper<ProfileViewEventDto>> mappers,
                                    JsonMapper jsonMapper,
                                    AnalyticsEventService analyticsEventService) {
        super(mappers, jsonMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEventDto.class, EventType.PROFILE_VIEW);
        log.info("Data successfully passed to analyticsEventService");
    }
}

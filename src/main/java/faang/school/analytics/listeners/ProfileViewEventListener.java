package faang.school.analytics.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.services.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileViewEventListener extends AbstractListener<ProfileViewEventDto> implements MessageListener {
    private final AnalyticsEventMapper analyticsEventMapper;

    public ProfileViewEventListener(AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    ObjectMapper objectMapper) {
        super(analyticsEventService, objectMapper);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEventDto.class, (event) -> analyticsEventMapper.toAnalyticsEvent((ProfileViewEventDto) event));
        log.info("Data successfully passed to analyticsEventService");
    }
}

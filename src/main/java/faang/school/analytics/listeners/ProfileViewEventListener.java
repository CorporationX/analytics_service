package faang.school.analytics.listeners;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.mappers.JsonMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.services.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final JsonMapper jsonMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEventDto profileViewEventDto = jsonMapper.toEvent(message.getBody(), ProfileViewEventDto.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(profileViewEventDto);
        analyticsEventService.gatherAnalyticsByProfileViews(analyticsEvent);
        log.info("Data successfully passed to analyticsEventService");
    }
}

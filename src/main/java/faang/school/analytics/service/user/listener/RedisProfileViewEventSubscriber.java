package faang.school.analytics.service.user.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.listener.RedisEventSubscriberBatchSave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class RedisProfileViewEventSubscriber extends RedisEventSubscriberBatchSave<ProfileViewEventDto> {
    public RedisProfileViewEventSubscriber(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper,
                                           AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Function<List<ProfileViewEventDto>, List<AnalyticsEvent>> toEventsFunction =
                profileViewEventDtos -> getAnalyticsEventMapper().toAnalyticsEvents(profileViewEventDtos);
        addToList(message, ProfileViewEventDto.class, toEventsFunction);
    }
}

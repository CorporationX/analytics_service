package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.FollowerEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    private final FollowerEventMapper followerEventMapper;

    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, FollowerEventMapper followerEventMapper) {
        super(objectMapper, analyticsEventService);
        this.followerEventMapper = followerEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.handleEvent(FollowerEventDto.class, message);
    }

    @Override
    AnalyticsEvent map(FollowerEventDto followerEventDto) {
        return followerEventMapper.toEntity(followerEventDto);
    }
}

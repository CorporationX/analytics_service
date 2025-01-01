package faang.school.analytics.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.model.dto.FollowerEvent;
import faang.school.analytics.model.mapper.FollowerEventMapper;
import faang.school.analytics.service.FollowerEventService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

 private final FollowerEventService followerEventService;
 private final FollowerEventMapper followerEventMapper;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 FollowerEventService followerEventService,
                                 FollowerEventMapper followerEventMapper) {
        super(objectMapper);
        this.followerEventService = followerEventService;
        this.followerEventMapper = followerEventMapper;
    }
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, event ->
                followerEventService.save(followerEventMapper.toAnalyticsEventDto(event)));
    }
}
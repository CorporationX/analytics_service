package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEventDto> {

    @Autowired
    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 PostViewEventMapper postViewEventMapper) {
        super(objectMapper, analyticsEventService, postViewEventMapper);
    }

    @Override
    protected Class<PostViewEventDto> getEventType() {
        return PostViewEventDto.class;
    }
}
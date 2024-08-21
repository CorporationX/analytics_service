package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.PostViewEvent;
import faang.school.analytics.mapper.PostViewMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {


    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 PostViewMapper mapper) {
        super(objectMapper, analyticsEventService, mapper);
    }

    @Override
    protected Class<PostViewEvent> getInstance() {
        return PostViewEvent.class;
    }
}

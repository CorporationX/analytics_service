package faang.school.analytics.listener.postview;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {

    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsService analyticsService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsService, analyticsEventMapper, PostViewEvent.class);
    }

    @Override
    protected AnalyticsEvent mapEvent(PostViewEvent event) {
        return analyticsEventMapper.toAnalyticsPostEvent(event);
    }
}

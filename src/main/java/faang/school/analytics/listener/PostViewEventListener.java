package faang.school.analytics.listener;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @EventListener
    public void handleFollowerEvent(FollowerEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
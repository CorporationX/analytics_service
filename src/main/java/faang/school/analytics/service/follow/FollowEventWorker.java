package faang.school.analytics.service.follow;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowEventWorker {
    private final AnalyticsEventService analyticsEventService;

    public void saveFollowEvent(FollowEventDto followEventDto) {
        analyticsEventService.followEventSave(followEventDto);
    }
}
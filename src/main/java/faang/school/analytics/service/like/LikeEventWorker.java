package faang.school.analytics.service.like;

import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeEventWorker {
    private final AnalyticsEventService analyticsEventService;

    public void saveLikeEvent(LikeEventDto likeEventDto) {
        analyticsEventService.likeEventSave(likeEventDto);
    }
}

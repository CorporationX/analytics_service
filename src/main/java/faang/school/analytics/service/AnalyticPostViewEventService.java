package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticPostViewEventService {
    private final PostViewEventMapper postViewEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public void savePostViewEvent(PostViewEventDto event) {
        AnalyticsEventDto analyticsEventDto = postViewEventMapper.toEntity(event);
        analyticsEventService.saveEvent(analyticsEventDto);
    }
}

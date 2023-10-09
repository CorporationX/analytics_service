package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.PostViewMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewAnalytic<T> implements AnalyticService<PostViewEventDto> {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final PostViewMapper postViewMapper;

    @Override
    public void save(PostViewEventDto eventDto) {
        AnalyticsEvent event = postViewMapper.ToAnalyticsEvent(eventDto);
        event.setEventType(EventType.POST_VIEW);
        analyticsEventRepository.save(event);
    }

    @Override
    public boolean supportsEventType(PostViewEventDto eventType) {
        return eventType.getClass() == PostViewEventDto.class;
    }
}

package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.PostViewMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final PostViewMapper postViewMapper;

    @Transactional
    public AnalyticsEvent savePostViewEvent(PostViewEventDto postViewEventDto){
        AnalyticsEvent event = postViewMapper.ToAnalyticsEvent(postViewEventDto);
        event.setEventType(EventType.POST_VIEW);

        return analyticsEventRepository.save(event);
    }
}

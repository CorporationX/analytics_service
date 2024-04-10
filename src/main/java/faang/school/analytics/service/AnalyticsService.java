package faang.school.analytics.service;

import faang.school.analytics.event.postview.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public void savePostViewEvent(PostViewEvent postViewEvent) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEventMapper.toAnalyticsEvent(postViewEvent));
        log.info("Event saved: " + savedEvent);
    }
}

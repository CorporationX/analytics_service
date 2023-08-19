package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    public void savePostEvent(PostViewEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);

        repository.save(analyticsEvent);

        log.info("Saved analytics event: {}", analyticsEvent);
    }
}

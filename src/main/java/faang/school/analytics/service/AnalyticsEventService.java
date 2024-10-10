package faang.school.analytics.service;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveLikeEvent(LikeEvent likeEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(likeEvent);
        analyticsEventRepository.save(analyticsEvent);
        log.info("likeEvent saved to database");
    }

}

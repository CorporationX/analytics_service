package faang.school.analytics.service.impl.analyticsevent;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public void saveFollowerEvent(FollowerEventDto dto) {
        AnalyticsEvent entity = analyticsEventMapper.toEntity(dto);
        entity.setEventType(EventType.FOLLOWER);
        log.info("Saving follower event: {}", entity);
        analyticsEventRepository.save(entity);
        log.info("Saved follower event: {}", entity);
    }
}

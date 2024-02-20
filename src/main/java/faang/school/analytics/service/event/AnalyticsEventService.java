package faang.school.analytics.service.event;

import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
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

    public void saveAnalyticsEvent(FollowerEventDto followerEventDto) {
        AnalyticsEvent followerEvent = analyticsEventMapper.toEntity(followerEventDto);
        followerEvent.setEventType(EventType.FOLLOWER);
        log.info("Старт saveAnalyticsEvent: {}", followerEventDto);
        analyticsEventRepository.save(followerEvent);
        log.info("Сохранен AnalyticsEvent - {} ", followerEvent);
    }
}
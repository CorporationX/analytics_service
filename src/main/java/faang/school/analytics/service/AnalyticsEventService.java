package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
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
    private final PostViewEventMapper eventMapper;

    public void saveEvent(PostViewEventDto eventDto) {
        AnalyticsEvent analyticsEvent = eventMapper.toEntity(eventDto);
        analyticsEvent.setEventType(EventType.POST_VIEW);

        analyticsEventRepository.save(analyticsEvent);
        log.info("AnalyticsEvent: {} was saved successfully in DB", analyticsEvent.getEventType());
    }
}

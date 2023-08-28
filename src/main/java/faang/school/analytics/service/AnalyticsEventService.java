package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final EventMapper eventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final FollowEventMapper followEventMapper;

    @Transactional
    public void followEventSave(FollowEventDto followEventDto) {
        var event = followEventMapper.toEntity(followEventDto);
        analyticsEventRepository.save(event);
    }

    @Transactional
    public void commentEventSave(CommentEventDto commentEventDto) {
        AnalyticsEvent analyticsEvent = eventMapper.toEntity(commentEventDto);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        analyticsEventRepository.save(analyticsEvent);
    }
}

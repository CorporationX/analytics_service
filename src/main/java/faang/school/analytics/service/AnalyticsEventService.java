package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnalyticsEventService {
    private final EventMapper eventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    @Transactional
    public void followEventSave(FollowEventDto followEventDto) {
        var event = eventMapper.toEntity(followEventDto);
        analyticsEventRepository.save(event);
    }

    @Transactional
    public void likeEventSave(LikeEventDto likeEventDto) {
        AnalyticsEvent event = mapper.toEvent(likeEventDto);
        analyticsEventRepository.save(event);
    }

    @Transactional
    public void commentEventSave(CommentEventDto commentEventDto) {
        AnalyticsEvent analyticsEvent = eventMapper.toEntity(commentEventDto);
        analyticsEvent.setReceivedAt(LocalDateTime.now()); // В бд это поле и так заполняется дефолтно
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional
    public List<AnalyticsDto> getAnalytics(AnalyticsFilterDto filterDto) {
        var analyticList = analyticsEventRepository.findByReceiverIdAndEventType(filterDto.getReceiverId(), filterDto.getEventType())
                .stream()
                .filter(event -> isInTimeInterval(event, filterDto))
                .map(eventMapper::toDto)
                .toList();
        return analyticList;
    }

    public void savePostEvent(PostViewEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);

        analyticsEventRepository.save(analyticsEvent);

        log.info("Saved analytics event: {}", analyticsEvent);
    }

    public void save(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved analytics event: {}", event);
    }

    private boolean isInTimeInterval(AnalyticsEvent event, AnalyticsFilterDto filterDto) {
        LocalDateTime receivedAt = event.getReceivedAt();
        LocalDateTime start = filterDto.getStart(), end = filterDto.getEnd();
        return (end == null || !receivedAt.isAfter(end)) && (start == null || !receivedAt.isBefore(start));
    }
}

package faang.school.analytics.service.events;

import faang.school.analytics.client.user.UserServiceClient;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventFilterDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventFilter analyticsEventFilter;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public AnalyticsEventDto saveEvent(AnalyticsEventDto eventDto) {
        userContext.setUserId(1L);
        userServiceClient.getUser(eventDto.getReceiverId());
        eventDto.setReceivedAt(LocalDateTime.now());
        log.info("getting action: {}, from userId: {}", eventDto, eventDto.getReceiverId());

        if (eventDto.getId() != null) {
            throw new DataValidationException("The event must not have id for save");
        }

        eventDto.setReceivedAt(LocalDateTime.now());
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        event = analyticsEventRepository.save(event);
        log.info("success saved action of userId: {}", eventDto.getReceiverId());
        return analyticsEventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filter) {
        if (filter.getInterval() != null && (filter.getFrom() != null || filter.getTo() != null)) {
            log.warn("Incorrect filter for get events: interval = {}, fromAt = {}, toAt = {}", filter.getInterval(), filter.getFrom(), filter.getTo());
            throw new DataValidationException("Search filter required 'Interval' or 'Dates'");
        }

        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(filter.getReceiverId(), filter.getEventType());
        events = filterEvents(events, filter);
        return events
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    public Map<Long, Integer> mapAnalyticEventsToActorActionsCount(List<AnalyticsEvent> analyticsEvents) {
        return analyticsEvents.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsEvent::getActorId,
                        HashMap::new,
                        Collectors.summingInt(event -> 1)
                ));
    }

    public int getSumOfUsersActionsByEventType(List<Integer> usersActionsSumByEventType) {
        return usersActionsSumByEventType.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public AnalyticsEventDto savePostView(AnalyticsEventDto analyticsEventDto) {
        log.info("saving view, post id: {}", analyticsEventDto.getReceiverId());
        AnalyticsEvent event = analyticsEventMapper.toEntity(analyticsEventDto);
        return analyticsEventMapper.toDto(analyticsEventRepository.save(event));
    }

    private Stream<AnalyticsEvent> filterEvents(Stream<AnalyticsEvent> events, AnalyticsEventFilterDto filter) {
        if (filter.getInterval() == null && filter.getFrom() == null && filter.getTo() == null) {
            return events;
        }
        return filter.getInterval() != null ? analyticsEventFilter.filterByInterval(events, filter.getInterval().getDays()) :
                analyticsEventFilter.filterByDates(events, filter.getFrom(), filter.getTo());
    }
}

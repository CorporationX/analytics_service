package faang.school.analytics.service.analytic;

import faang.school.analytics.client.user.UserServiceClient;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public ResponseEntity<Void> saveAction(AnalyticsEventDto analyticsEventDto) {
        userContext.setUserId(1L);
        userServiceClient.getUser(analyticsEventDto.getReceiverId());
        analyticsEventDto.setReceivedAt(LocalDateTime.now());
        log.info("getting action: {}, from userId: {}", analyticsEventDto, analyticsEventDto.getReceiverId());
        analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        log.info("success saved action of userId: {}", analyticsEventDto.getReceiverId());
        return ResponseEntity.ok().build();
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
        AnalyticsEvent event = analyticsEventMapper.toEntity(analyticsEventDto);
        return analyticsEventMapper.toDto(analyticsEventRepository.save(event));
    }
}

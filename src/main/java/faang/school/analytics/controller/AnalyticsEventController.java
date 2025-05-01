package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsRequestDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper eventMapper;

    @PostMapping
    public ResponseEntity<List<AnalyticsEventDto>> getAnalytics(@RequestBody AnalyticsRequestDto requestDto) {

        log.info("Received analytics request for receiverId={}, eventType={}, interval={}, startDate={}, endDate={}",
                requestDto.getReceiverId(),
                requestDto.getEventType(),
                requestDto.getInterval(),
                requestDto.getStartDate(),
                requestDto.getEndDate());

        List<AnalyticsEvent> events = analyticsEventService.getParseAnalytics(
                requestDto.getReceiverId(),
                requestDto.getEventType(),
                requestDto.getInterval(),
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );

        log.info("Analytics data returned: {} events", events.size());

        return ResponseEntity.ok(eventMapper.toAnalyticsEventDtoList(events));
    }
}



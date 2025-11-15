package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsEventController {

        private final AnalyticsEventService analyticsEventService;

        @PostMapping("/events")
        void saveEvent(@RequestBody EventDto eventDto) {
            log.info("Save analytics event - receiver: {}, type: {}", eventDto.receiverId(), eventDto.eventType());
            analyticsEventService.saveEvent((AnalyticsEventDto) eventDto);
        }

        @GetMapping
        List<AnalyticsEventResponseDto> getAnalytics(@RequestParam long receiverId,
                                                     @RequestParam EventType eventType,
                                                     @RequestParam(required = false) Interval interval,
                                                     @RequestParam(required = false) LocalDateTime from,
                                                     @RequestParam(required = false) LocalDateTime to) {

            log.info("Get analytics for receiver: {}, type: {}", receiverId, eventType);
            return analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);
        }
    }

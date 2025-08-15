package faang.school.analytics.controller;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.service.MentorshipEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mentorship")
@RequiredArgsConstructor
public class MentorshipAnalyticsController {

    private final MentorshipEventService analyticsServer;

    @GetMapping("/mentorshiprequest/{userId}")
    public List<MentorshipEventDto> getUserMentorshipAnalytics(
            @PathVariable long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {


        return analyticsServer.getUserMentorshipAnalytics(userId, from, to);
    }
}
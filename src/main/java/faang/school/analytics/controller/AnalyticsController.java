package faang.school.analytics.controller;

import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track/profile-view")
    @ResponseStatus(HttpStatus.CREATED)
    public void trackProfileView(@RequestParam long actorId, @RequestParam long receiverId) {
        analyticsService.trackProfileView(actorId, receiverId);
    }
}

package faang.school.analytics.controller;

import faang.school.analytics.model.service.impl.AnalyticsEventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsEventServiceImpl analyticsEventServiceImpl;

    @GetMapping("/getAnalytics")
    public void getAnalytics() {
        
    }
}

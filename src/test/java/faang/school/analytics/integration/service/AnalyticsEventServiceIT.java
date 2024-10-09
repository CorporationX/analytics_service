package faang.school.analytics.integration.service;

import faang.school.analytics.dto.analyticsevent.AnalyticsEventDto;
import faang.school.analytics.integration.IntegrationTestBase;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsEventServiceIT extends IntegrationTestBase {
    private final AnalyticsEventService analyticsEventService;

    @Autowired
    public AnalyticsEventServiceIT(AnalyticsEventService analyticsEventService) {
        this.analyticsEventService = analyticsEventService;
    }

    @Test
    @DisplayName("Send Event Test")
    void testSendEvent() {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(2)
                .actorId(3)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2023-06-01T15:00:00"))
                .build();
        var result = analyticsEventService.sendEvent(event);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(event);
    }

    @Test
    @DisplayName("Get Analytics Event Test With Interval Not Null")
    void testGetAnalyticsEventsWithIntervalNotNull() {
        var expected = AnalyticsEventDto.builder()
                .id(3L)
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.now().minusDays(1))
                .build();
        var result = analyticsEventService.getAnalytics(2, EventType.PROFILE_VIEW, Interval.WEEK, null, null);
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().ignoringFields("receivedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Get Analytics Event Test With Interval is Null")
    void testGetAnalyticsEventsWithIntervalIsNull() {
        var from = LocalDateTime.parse("2023-06-01T15:00:00");
        var to = LocalDateTime.parse("2024-12-01T15:00:00");
        var element1 = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2024-06-01T15:00:00"))
                .build();

        var element2 = AnalyticsEventDto.builder()
                .id(2L)
                .receiverId(1L)
                .actorId(3L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2024-07-01T14:00:00"))
                .build();

        var expected = List.of(element1, element2);
        var result = analyticsEventService.getAnalytics(1, EventType.PROFILE_VIEW, null, from, to);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result).isEqualTo(expected);
    }
}

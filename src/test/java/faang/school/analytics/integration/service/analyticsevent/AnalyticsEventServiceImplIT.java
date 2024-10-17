package faang.school.analytics.integration.service.analyticsevent;

import faang.school.analytics.integration.IntegrationTestBase;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsEventServiceImplIT extends IntegrationTestBase {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    public AnalyticsEventServiceImplIT(AnalyticsEventService analyticsEventService, AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @Test
    @DisplayName("Save Event Test")
    void testSaveEvent() {
        var givenAnalyticsEvent = AnalyticsEvent.builder()
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2023-06-01T15:00:00"))
                .build();

        var expected = AnalyticsEvent.builder()
                .id(4L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2023-06-01T15:00:00"))
                .build();

        analyticsEventService.saveEvent(givenAnalyticsEvent);
        var savedEntity = analyticsEventRepository.findById(expected.getId());
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get()).isNotNull();
        assertThat(savedEntity.get()).isEqualTo(expected);
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
                .id(2L)
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2024-06-01T15:00:00"))
                .build();

        var element2 = AnalyticsEventDto.builder()
                .id(1L)
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

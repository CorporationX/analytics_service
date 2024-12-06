package faang.school.analytics.validator.analytic_event;

import faang.school.analytics.dto.event.EventRequestDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.event.Interval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticAnalyticsEventServiceValidatorTest {
    private AnalyticEventServiceValidator analyticEventServiceValidator;
    private AnalyticsEvent analyticsEvent;
    private EventRequestDto eventRequestDto;

    @BeforeEach
    void setUp() {
        analyticEventServiceValidator = new AnalyticEventServiceValidator();
        analyticsEvent = new AnalyticsEvent();
        eventRequestDto = new EventRequestDto();
    }

    @Test
    void testInvalidAnalyticEvent() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticEventServiceValidator.checkEntity(analyticsEvent));
    }

    @Test
    void testCorrectAnalyticEventWork() {
        analyticsEvent.setEventType(EventType.FOLLOWER);
        analyticsEvent.setActorId(21);
        analyticsEvent.setReceiverId(1);

        assertDoesNotThrow(() -> analyticEventServiceValidator.checkEntity(analyticsEvent));
    }

    @Test
    void testInvalidEventRequestDto() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticEventServiceValidator.checkRequestDto(eventRequestDto));
    }

    @Test
    void testCorrectEventRequestDto() {
        eventRequestDto.setInterval(Interval.WEEK);

        assertDoesNotThrow(() -> analyticEventServiceValidator.checkRequestDto(eventRequestDto));
    }
}
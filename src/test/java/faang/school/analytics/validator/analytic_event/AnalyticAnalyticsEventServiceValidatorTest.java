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

    @BeforeEach
    void setUp() {
        analyticEventServiceValidator = new AnalyticEventServiceValidator();
        analyticsEvent = new AnalyticsEvent();
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
    void testInvalidInterval() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticEventServiceValidator.validateInterval(null, null, null));
    }

    @Test
    void testCorrectInterval() {
        assertDoesNotThrow(() -> analyticEventServiceValidator.validateInterval(Interval.WEEK, null, null));
    }

    @Test
    void testInvalidCheckIdAndEvent() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticEventServiceValidator.checkIdAndEvent(-12, null));
    }

    @Test
    void testCorrectCheckIdAndEvent() {
        assertDoesNotThrow(() -> analyticEventServiceValidator.checkIdAndEvent(1, EventType.FOLLOWER));
    }
}
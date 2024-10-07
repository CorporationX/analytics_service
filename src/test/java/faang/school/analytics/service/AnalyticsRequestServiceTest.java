package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsRequestParams;
import faang.school.analytics.exception.exceptions.InvalidDateException;
import faang.school.analytics.exception.exceptions.InvalidEventTypeException;
import faang.school.analytics.exception.exceptions.InvalidIntervalException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AnalyticsRequestServiceTest {

    @InjectMocks
    private AnalyticsRequestService analyticsRequestService;

    private static final String VALID_EVENT_TYPE_INDEX = "0";
    private static final String VALID_EVENT_TYPE_NAME = "PROFILE_VIEW";
    private static final String INVALID_EVENT_TYPE = "INVALID_TYPE";

    private static final String VALID_INTERVAL_INDEX = "1";
    private static final String VALID_INTERVAL_NAME = "LAST_WEEK";
    private static final String INVALID_INTERVAL = "INVALID_INTERVAL";

    private static final String VALID_DATE_TIME = "2023-10-07T12:00:00";
    private static final String INVALID_DATE_TIME = "2023-10-07 12:00:00";

    private static final String FROM_DATE = "2023-10-01T00:00:00";
    private static final String TO_DATE = "2023-10-07T23:59:59";


    @Test
    void testConvertToEventType_validIndex() {
        EventType result = analyticsRequestService.convertToEventType(VALID_EVENT_TYPE_INDEX);
        assertEquals(EventType.values()[0], result);
    }

    @Test
    void testConvertToEventType_validName() {
        EventType result = analyticsRequestService.convertToEventType(VALID_EVENT_TYPE_NAME);
        assertEquals(EventType.PROFILE_VIEW, result);
    }

    @Test
    void testConvertToEventType_invalidInput() {
        assertThrows(InvalidEventTypeException.class, () ->
            analyticsRequestService.convertToEventType(INVALID_EVENT_TYPE));
    }

    @Test
    void testConvertToInterval_validIndex() {
        Interval result = analyticsRequestService.convertToInterval(VALID_INTERVAL_INDEX);
        assertEquals(Interval.values()[1], result);
    }

    @Test
    void testConvertToInterval_validName() {
        Interval result = analyticsRequestService.convertToInterval(VALID_INTERVAL_NAME);
        assertEquals(Interval.LAST_WEEK, result);
    }

    @Test
    void testConvertToInterval_invalidInput() {
        assertThrows(InvalidIntervalException.class, () ->
            analyticsRequestService.convertToInterval(INVALID_INTERVAL));
    }

    @Test
    void testConvertToLocalDateTime_validDateTime() {
        LocalDateTime result = analyticsRequestService.convertToLocalDateTime(VALID_DATE_TIME);
        assertEquals(LocalDateTime.of(2023, 10, 7, 12, 0, 0), result);
    }

    @Test
    void testConvertToLocalDateTime_invalidDateTime() {
        assertThrows(InvalidDateException.class, () ->
                analyticsRequestService.convertToLocalDateTime(INVALID_DATE_TIME));
    }

    @Test
    void testProcessRequestParams_validParams() {
        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_INDEX, VALID_INTERVAL_NAME, FROM_DATE, TO_DATE);
        assertNotNull(params);
        assertEquals(EventType.values()[0], params.getEventType());
        assertEquals(Interval.LAST_WEEK, params.getInterval());
        assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), params.getFrom());
        assertEquals(LocalDateTime.of(2023, 10, 7, 23, 59, 59), params.getTo());
    }

    @Test
    void testProcessRequestParams_invalidParams() {
        assertThrows(InvalidEventTypeException.class, () ->
            analyticsRequestService.processRequestParams(INVALID_EVENT_TYPE, null, null, null));
    }
}
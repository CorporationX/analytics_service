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
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AnalyticsRequestServiceTest {
    @InjectMocks
    private AnalyticsRequestService analyticsRequestService;

    private static final String VALID_EVENT_TYPE_NAME = "PROFILE_VIEW";
    private static final String INVALID_EVENT_TYPE = "INVALID_TYPE";
    private static final String VALID_INTERVAL_NAME = "LAST_WEEK";
    private static final String INVALID_INTERVAL = "INVALID_INTERVAL";

    private static final String VALID_DATE_TIME = "2023-10-07T12:00:00";
    private static final String INVALID_DATE_TIME = "2023-10-07 12:00:00";

    private static final String FROM_DATE = "2023-10-01T00:00:00";
    private static final String TO_DATE = "2023-10-07T23:59:59";

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
    void testProcessRequestParams_withInterval() {
        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(
                VALID_EVENT_TYPE_NAME, VALID_INTERVAL_NAME, null, null);
        assertNotNull(params);
        assertEquals(EventType.PROFILE_VIEW, params.getEventType());
        assertEquals(Interval.LAST_WEEK, params.getInterval());
        assertNull(params.getFrom());
        assertNull(params.getTo());
    }

    @Test
    void testProcessRequestParams_withFromAndTo() {
        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(
                VALID_EVENT_TYPE_NAME, null, FROM_DATE, TO_DATE);
        assertNotNull(params);
        assertEquals(EventType.PROFILE_VIEW, params.getEventType());
        assertEquals(Interval.ALL_TIME, params.getInterval());
        assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), params.getFrom());
        assertEquals(LocalDateTime.of(2023, 10, 7, 23, 59, 59), params.getTo());
    }


    @Test
    void testProcessRequestParams_withDefaultFromAndTo() {
        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(
                VALID_EVENT_TYPE_NAME, null, null, null);
        assertNotNull(params);
        assertEquals(EventType.PROFILE_VIEW, params.getEventType());
        assertEquals(Interval.ALL_TIME, params.getInterval());
        assertEquals(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC), params.getFrom());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(params.getTo().isAfter(now.minusSeconds(5)) && params.getTo().isBefore(now.plusSeconds(5)));
    }

    @Test
    void testProcessRequestParams_withIntervalAndFromTo() {
        assertThrows(IllegalArgumentException.class, () ->
                analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_NAME, VALID_INTERVAL_NAME, FROM_DATE, TO_DATE));
    }

    @Test
    void testProcessRequestParams_invalidEventType() {
        assertThrows(InvalidEventTypeException.class, () ->
                analyticsRequestService.processRequestParams(INVALID_EVENT_TYPE, null, null, null));
    }

    @Test
    void testProcessRequestParams_invalidInterval() {
        assertThrows(InvalidIntervalException.class, () ->
                analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_NAME, INVALID_INTERVAL, null, null));
    }

    @Test
    void testProcessRequestParams_fromDateAfterToDate() {
        String invalidFromDate = "2023-10-08T00:00:00";
        assertThrows(IllegalArgumentException.class, () ->
                analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_NAME, null, invalidFromDate, TO_DATE));
    }

    @Test
    void testProcessRequestParams_invalidFromDateFormat() {
        assertThrows(InvalidDateException.class, () ->
                analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_NAME, null, INVALID_DATE_TIME, TO_DATE));
    }

    @Test
    void testProcessRequestParams_invalidToDateFormat() {
        assertThrows(InvalidDateException.class, () ->
                analyticsRequestService.processRequestParams(VALID_EVENT_TYPE_NAME, null, FROM_DATE, INVALID_DATE_TIME));
    }

    @Test
    void testProcessRequestParams_nullFromAndToWithNullInterval() {
        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(
                VALID_EVENT_TYPE_NAME, null, null, null);
        assertNotNull(params);
        assertEquals(Interval.ALL_TIME, params.getInterval());
        assertNotNull(params.getFrom());
        assertNotNull(params.getTo());
    }
}
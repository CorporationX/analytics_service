package faang.school.analytics.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Exceptional {

    public static final String INVALID_DATE_RANGE = "Некорректный диапазон дат: 'from' не может быть позже 'to'";
    public static final String INVALID_IDS = "SenderId и ReceiverId должны быть положительными";
    public static final String NULL_TIMESTAMP = "Timestamp не может быть null";

    public static void throwInvalidDateRange() {
        log.error(INVALID_DATE_RANGE);
        throw new AnalyticsException(INVALID_DATE_RANGE);
    }

    public static void throwInvalidIds() {
        log.error(INVALID_IDS);
        throw new AnalyticsException(INVALID_IDS);
    }

    public static void throwNullTimestamp() {
        log.error(NULL_TIMESTAMP);
        throw new AnalyticsException(NULL_TIMESTAMP);
    }
}
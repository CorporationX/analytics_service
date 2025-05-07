package faang.school.analytics.constants;

public class Constants {
    public static final String INVALID_INTERVAL = "Invalid interval: ";
    public static final String INVALID_EVENT_TYPE = "Invalid event type: ";
    public static final String INVALID_DATE_FORMAT = "Invalid date format: ";
    public static final String MISSING_DATE_PARAMS = "You must provide either an interval or both startDate and endDate.";
    public static final String DATE_FORMAT = "dd-MM-yyyy'T'HH:mm:ss";
    public static final String FROM_OR_TO_NULL_EXCEPTION = "Both 'from' and 'to' must be provided" +
            " when interval is null";
    public static final String EVENT_TYPE_NULL_EXCEPTION = "Event type can't be null";
    public static final String EVENT_NULL_EXCEPTION = "Event can't be null";
}

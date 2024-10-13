package faang.school.analytics.validator;

public class AnalyticsEventValidator {

    public void intervalOrStartAndEndDate(String interval, String startDate, String endDate) {
        if ((interval != null && (startDate != null || endDate != null)) ||
                (interval == null && (startDate == null || endDate == null))) {
            throw new IllegalArgumentException("You must provide either 'interval' or both 'startDate' and 'endDate', but not all together.");
        }
    }
}

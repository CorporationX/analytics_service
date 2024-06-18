package faang.school.analytics.model.interval;

import lombok.ToString;

@ToString
public enum TypeOfInterval {
    MINUTES("Minutes"),
    HOURS("Hours"),
    DAYS("Days"),
    WEEKS("Weeks"),
    MONTH("Month"),
    YEARS("Years");

    private final String interval;

    TypeOfInterval(String interval) {
        this.interval = interval;
    }
}

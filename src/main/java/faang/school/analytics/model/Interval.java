package faang.school.analytics.model;

import java.time.LocalDateTime;

public enum Interval {
    YEAR("Год"),
    MONTH("Месяц"),
    WEEK("Неделя"),
    DAY("День"),
    HOUR("Час"),
    MINUTE("Минутa");

    private String title;
    private static final long OFFSET_DATA_TIME = 1l;

    Interval(String title) {
    }

    //Определение интервала времени со смещением 1 (в каждой гранулярности) от входящей конечной даты endDateTime
    public LocalDateTime getStartDataTime(Interval interval, LocalDateTime endDateTime) {
        switch (interval) {
            case YEAR -> {
                return endDateTime.minusYears(OFFSET_DATA_TIME);
            }
            case MONTH -> {
                return endDateTime.minusMonths(OFFSET_DATA_TIME);
            }
            case WEEK -> {
                return endDateTime.minusWeeks(OFFSET_DATA_TIME);
            }
            case DAY -> {
                return endDateTime.minusDays(OFFSET_DATA_TIME);
            }
            case HOUR -> {
                return endDateTime.minusHours(OFFSET_DATA_TIME);
            }
            case MINUTE -> {
                return endDateTime.minusMinutes(OFFSET_DATA_TIME);
            }
            default -> {
                return null;
            }
        }
    }
}
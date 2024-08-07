package faang.school.analytics.model;

import faang.school.analytics.exception.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Interval {
    DAY, WEEK, MONTH, YEAR, ALL_TIME;

    public static Interval conversionToInterval(String intervalString) {
        if (intervalString == null) {
            return null;
        }
        try {
            return Interval.valueOf(intervalString.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                int ordinal = Integer.parseInt(intervalString);
                return Interval.values()[ordinal];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e2) {
                log.error(ExceptionMessages.INVALID_INPUT_IS_SUPPLIED, e2);
                throw new IllegalArgumentException(ExceptionMessages.INVALID_INPUT_IS_SUPPLIED, e2);
            }
        }
    }
}
package faang.school.analytics.model;

public enum Interval {
    DAY, WEEK, MONTH, YEAR, ALL_TIME;

    public static Interval conversionToInterval(String intervalString){
        if (intervalString == null){
            return null;
        }
        try {
            return Interval.valueOf(intervalString.toUpperCase());
        } catch (IllegalArgumentException e){
            int ordinal = Integer.parseInt(intervalString);
            return Interval.values()[ordinal];
        }
    }
}
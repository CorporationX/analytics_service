package faang.school.analytics.exception;

public final class ExceptionMessages {
    private ExceptionMessages(){
    }
    public static final String INVALID_TRANSFORMATION = "Failed to convert message to objectMapper.";
    public static final String ARGUMENT_NOT_FOUND = "Either interval or both from and to dates must be provided.";
    public static final String INTERVAL_NOT_FOUND = "There is no such space - ";
    public static final String INVALID_INPUT_IS_SUPPLIED = "Cannot convert. Wrong data entered.";

    public static final String FAILED_PERSISTENCE = "Failed to save data to database. Please review the data and try again.";

}
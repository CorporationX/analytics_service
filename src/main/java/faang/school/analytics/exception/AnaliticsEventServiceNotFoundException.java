package faang.school.analytics.exception;

import faang.school.analytics.dto.ErrorType;
import org.slf4j.helpers.MessageFormatter;


public class AnaliticsEventServiceNotFoundException extends NonRetryableException {
    public AnaliticsEventServiceNotFoundException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }

    public AnaliticsEventServiceNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
    }
}

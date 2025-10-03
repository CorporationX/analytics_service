package faang.school.analytics.exception;

import faang.school.analytics.dto.ErrorType;
import org.slf4j.helpers.MessageFormatter;

public class ProcessorNotFoundException extends NonRetryableException {
    public ProcessorNotFoundException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }

    public ProcessorNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
    }
}

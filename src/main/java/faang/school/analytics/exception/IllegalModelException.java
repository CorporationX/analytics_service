package faang.school.analytics.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalModelException extends RuntimeException {
    public IllegalModelException(String message) {
        super(message);
        log.error(message, this);
    }
}

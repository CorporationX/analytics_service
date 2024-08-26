package faang.school.analytics.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperReadValueException extends RuntimeException {
    public MapperReadValueException(String message) {
        super(message);
    }
}

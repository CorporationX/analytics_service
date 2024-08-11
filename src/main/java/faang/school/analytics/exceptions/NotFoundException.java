package faang.school.analytics.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}

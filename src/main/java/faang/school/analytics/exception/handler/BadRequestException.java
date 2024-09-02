package faang.school.analytics.exception.handler;

/**
 * @author Evgenii Malkov
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String s) {
        super(s);
    }
}

package faang.school.analytics.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_FOUND("Analytic event was not found for receiverId");
    private final String message;
}

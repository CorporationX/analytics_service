package faang.school.analytics.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    PROCESSOR_NOT_FOUND("Required message builder was not found"),
    NOTIFICATOR_NOT_FOUND("Required analitics service was not found");

    private final String errorMessage;
}
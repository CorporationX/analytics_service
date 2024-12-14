package faang.school.analytics.domain.dto.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorMessageDto {
    private final int statusCode;
    private final String message;
    @Builder.Default
    private final LocalDateTime dateTime = LocalDateTime.now();
}

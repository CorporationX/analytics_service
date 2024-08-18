package faang.school.analytics.handler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorMessage {

    LocalDateTime timestamp;

    String message;

    String causeMessage;
}

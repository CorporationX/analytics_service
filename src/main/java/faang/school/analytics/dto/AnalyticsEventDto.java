package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnalyticsEventDto {
    private Long id;

    @NotNull(message = "ReceiverId shouldn't be null")
    @Positive(message = "ReceiverId should be positive")
    private Long receiverId;

    @NotNull(message = "AuthorId shouldn't be null")
    @Positive(message = "AuthorId should be positive")
    private Long actorId;

    @NotNull(message = "EventType shouldn't be null")
    private EventType eventType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime receivedAt;
}
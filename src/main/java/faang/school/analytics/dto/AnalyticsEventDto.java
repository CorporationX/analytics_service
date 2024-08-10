package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
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
    @NotNull
    private LocalDateTime receivedAt;
}
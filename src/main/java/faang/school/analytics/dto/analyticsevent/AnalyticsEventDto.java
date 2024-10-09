package faang.school.analytics.dto.analyticsevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        @Positive(message = "Id can't be least than zero")
        Long id,

        @Positive
        Long receiverId,

        @NotNull
        Long actorId,

        @NotNull
        EventType eventType,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
        LocalDateTime receivedAt
) {
}

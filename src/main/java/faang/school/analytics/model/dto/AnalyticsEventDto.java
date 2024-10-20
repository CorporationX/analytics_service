package faang.school.analytics.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.enums.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

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

        @JsonFormat(shape = STRING)
        LocalDateTime receivedAt
) {
}

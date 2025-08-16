package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RecommendationReceivedEventDto(
        @JsonProperty(required = true) Long id,
        @JsonProperty(required = true) Long authorId,
        @JsonProperty(required = true) Long receiverId,
        @JsonProperty(required = true) LocalDateTime createdAt
) {
}

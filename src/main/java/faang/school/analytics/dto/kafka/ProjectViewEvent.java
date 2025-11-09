package faang.school.analytics.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectViewEvent(
        @JsonProperty("projectId") Long projectId,
        @JsonProperty("viewerId") Long viewerId
) {
}

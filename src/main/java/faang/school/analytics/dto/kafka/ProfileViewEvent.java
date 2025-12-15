package faang.school.analytics.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfileViewEvent(
        @JsonProperty("viewerId") Long viewerId,
        @JsonProperty("profileOwnerId") Long profileOwnerId
) {
}

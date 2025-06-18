package faang.school.analytics.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.model.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerEvent {
    private String followerId;
    private String targetId;
    private EventType eventType;
    @JsonProperty("occurredAt")
    private LocalDateTime timestamp;
}


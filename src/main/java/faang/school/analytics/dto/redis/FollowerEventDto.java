package faang.school.analytics.dto.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerEventDto {
    private Long followerId;
    private Long followeeId;
    private EventType eventType;
    private LocalDateTime timestamp;
}

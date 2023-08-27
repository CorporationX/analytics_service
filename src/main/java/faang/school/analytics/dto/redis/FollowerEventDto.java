package faang.school.analytics.dto.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private long userId;
    private long receiverId;
    private long actorId;
    private String eventType;
    private LocalDateTime viewedAt;
}
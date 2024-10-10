package faang.school.analytics.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEventDto {
    protected Long receiverId;
    protected Long actorId;
    protected LocalDateTime timestamp;
}

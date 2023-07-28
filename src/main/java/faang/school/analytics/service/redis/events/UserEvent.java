package faang.school.analytics.service.redis.events;

import faang.school.analytics.service.redis.types.UserEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent implements Serializable {
    private UserEventType type;
    private Long id;
}

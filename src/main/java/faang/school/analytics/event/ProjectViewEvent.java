package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProjectViewEvent implements Serializable {
    private final long projectId;
    private final long userId;
    private final LocalDateTime timestamp;
}

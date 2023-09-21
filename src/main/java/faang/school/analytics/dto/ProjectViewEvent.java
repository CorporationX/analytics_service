package faang.school.analytics.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ProjectViewEvent {
    private Long projectId;
    private Long userId;
    private LocalDateTime timestamp;
}

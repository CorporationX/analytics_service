package faang.school.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ProjectViewEvent {
    private long projectId;
    private long userId;
    private LocalDateTime eventTime;


}


package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectViewEvent {
    private long projectId;
    private long ownerId;
    private LocalDateTime receivedAt;
}
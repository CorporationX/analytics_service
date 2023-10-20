package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectViewDto {
    private Long projectId;
    private Long userId;
    private LocalDateTime dateTime;
}

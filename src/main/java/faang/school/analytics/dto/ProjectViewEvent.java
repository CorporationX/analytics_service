package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectViewEvent {
    @Positive
    private long projectId;
    @Positive
    private long userId;
    @NotNull
    private LocalDateTime timestamp;
}
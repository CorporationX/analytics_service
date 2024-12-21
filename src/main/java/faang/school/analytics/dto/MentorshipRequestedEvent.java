package faang.school.analytics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestedEvent {
    @NotNull
    @Min(0)
    private Long menteeId;
    @NotNull
    @Min(0)
    private Long mentorId;
    @NotNull
    LocalDateTime timestamp;
}

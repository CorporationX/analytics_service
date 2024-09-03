package faang.school.analytics.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewEvent {
    private Long id;
    @NotNull(message = "Receiver id can't be empty")
    private Long authorId;
    @NotNull(message = "Receiver id can't be empty")
    private Long userId;
    private LocalDateTime receivedAt;
}

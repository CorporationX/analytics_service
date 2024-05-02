package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewEvent {
    @NotNull
    private long observedId;
    @NotNull
    private long observerId;
    @NotNull
    private LocalDateTime viewedAt;
}

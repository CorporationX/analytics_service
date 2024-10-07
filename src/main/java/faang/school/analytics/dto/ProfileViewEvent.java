package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileViewEvent {

    @NotNull
    private long observedId;
    @NotNull
    private long observerId;
    @NotNull
    private LocalDateTime viewedAt;

}
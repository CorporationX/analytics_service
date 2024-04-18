package faang.school.analytics.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchAppearanceEvent {
    @NotNull
    private Long viewedUserId;
    @NotNull
    private Long viewerUserId;
    @NotNull
    private LocalDateTime viewingTime;
}

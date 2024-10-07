package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAppearanceEvent {

    @NotNull
    private Long viewedUserId;
    @NotNull
    private Long viewerUserId;
    @NotNull
    private LocalDateTime viewingTime;

}
package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecomendationEventDto {
    private Long recomendationId;
    @NotNull
    private Long authorId;
    @NotNull
    private Long receiverId;
    private ZonedDateTime dateTime;


}

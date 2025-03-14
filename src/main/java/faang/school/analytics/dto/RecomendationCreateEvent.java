package faang.school.analytics.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record RecomendationCreateEvent (@Positive Long recomendationId,
                                        @Positive Long authorId,
                                        @Positive Long recipientId,
                                        LocalDateTime  time){

}

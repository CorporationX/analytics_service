package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {

    @Positive
    @NotNull
    private Long id;

    @Positive
    @NotNull
    private Long receiverId;

    @Positive
    @NotNull
    private Long actorId;

    private EventType eventType;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime receivedAt;
}

package faang.school.analytics.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class FollowerEvent {

    @NotNull
    private long followerId;

    @NotNull
    private long followeeId;


    @NotNull
    private LocalDateTime subscriptionDateTime;


}
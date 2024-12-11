package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import faang.school.analytics.deserializer.BigDecimalDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdBoughtEvent {
    private Long postId;
    private Long actorId;
    private Long receiverId;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal paymentAmount;
    private Long adDuration;
    private LocalDateTime receivedAt;
}

package faang.school.analytics.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import faang.school.analytics.until.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEvent {
    private long postId;
    private long authorId;
    private long userId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime likedAt;
    private EventType type;
}
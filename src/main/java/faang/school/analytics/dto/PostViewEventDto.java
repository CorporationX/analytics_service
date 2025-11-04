package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEventDto {

    private long postId;

    @JsonProperty("userId")
    private long receiverId;

    @JsonProperty("authorId")
    private long actorId;

    @JsonProperty("viewedAt")
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private LocalDateTime receivedAt;
}

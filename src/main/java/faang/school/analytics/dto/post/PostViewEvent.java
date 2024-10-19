package faang.school.analytics.dto.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.dto.BaseEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostViewEvent extends BaseEvent {

    private long postId;
    private Long authorId;
    private Long userId;
    private LocalDateTime localDateTime;

    @JsonCreator
    public PostViewEvent(@JsonProperty("postId") long postId,
                         @JsonProperty("authorId") Long authorId,
                         @JsonProperty("userId") Long userId,
                         @JsonProperty("localDateTime") LocalDateTime localDateTime) {
        super(authorId, userId, localDateTime);
        this.postId = postId;
    }
}

package faang.school.analytics.event.comment;

import faang.school.analytics.model.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentEventType {
    CREATE(EventType.POST_COMMENT),
    UPDATE(EventType.COMMENT_UPDATE),
    DELETE(EventType.COMMENT_DELETE);

    private final EventType eventType;
}

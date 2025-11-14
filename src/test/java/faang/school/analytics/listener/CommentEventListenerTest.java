package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {
    @InjectMocks
    private CommentEventListener listener;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Captor
    ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    String json = """
            {
                "commentId": 10,
                "authorId": 5,
                "postId": 3,
                "createdAt": "2025-11-14T22:20:30"
            }
            """;
    String invalidJson = "{invalid json}";

    @BeforeEach
    public void prepareObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSuccessfullyCommentEventHandle() throws Exception {
        CommentEventDto dto = objectMapper.readValue(json, CommentEventDto.class);
        AnalyticsEvent event = new AnalyticsEvent();
        when(analyticsEventMapper.toEntity(dto)).thenReturn(event);

        listener.handleCommentEvent(json);

        verify(analyticsEventMapper).toEntity(dto);
        verify(analyticsEventService).saveEvent(analyticsEventCaptor.capture());
        assertEquals(event, analyticsEventCaptor.getValue());
    }

    @Test
    void testFailedInvalidJsonHandle() {
        assertDoesNotThrow(() -> listener.handleCommentEvent(invalidJson));
        verifyNoInteractions(analyticsEventMapper, analyticsEventService);
    }

    @Test
    void testNotFailedWhenSaveThrowsException() throws Exception {
        CommentEventDto dto = objectMapper.readValue(json, CommentEventDto.class);
        AnalyticsEvent event = new AnalyticsEvent();

        when(analyticsEventMapper.toEntity(dto)).thenReturn(event);
        doThrow(new RuntimeException("DB error")).when(analyticsEventService).saveEvent(event);

        assertDoesNotThrow(() -> listener.handleCommentEvent(json));
    }
}

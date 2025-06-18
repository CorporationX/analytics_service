package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.kafka.consumer.FollowerEventListener;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock  private ObjectMapper objectMapper;
    @Mock  private FollowEventMapper mapper;
    @Mock  private AnalyticsEventService service;

    @InjectMocks
    private FollowerEventListener listener;

    private static final String RAW_JSON = "{\"followerId\":1,\"targetId\":2,\"timestamp\":\"2025-06-18T12:00:00Z\"}";

    @Nested
    @DisplayName("listen() – happy path")
    class HappyPath {
        @Test
        void savesMappedEntity() throws Exception {
            // given
            FollowerEvent dto = new FollowerEvent();
            AnalyticsEvent entity = new AnalyticsEvent();

            when(objectMapper.readValue(RAW_JSON, FollowerEvent.class)).thenReturn(dto);
            when(mapper.toEntity(dto)).thenReturn(entity);

            // when
            listener.listen(RAW_JSON);

            // then
            verify(service).save(entity);
            verifyNoMoreInteractions(service);
        }
    }

    @Nested
    @DisplayName("listen() – invalid JSON")
    class ErrorPath {
        @Test
        void logsErrorAndDoesNotSaveOnParseFailure() throws Exception {
            // given
            when(objectMapper.readValue(any(String.class), eq(FollowerEvent.class)))
                    .thenThrow(new JsonProcessingException("boom") {});

            // when
            listener.listen(RAW_JSON);

            // then
            verify(service, never()).save(any());
            verifyNoInteractions(mapper);
        }
    }
}
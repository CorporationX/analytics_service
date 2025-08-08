package faang.school.analytics.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.event.RecommendationReceivedEventDto;
import faang.school.analytics.exception.event.EventDeserializationException;
import faang.school.analytics.mapper.RecommendationReceivedEventMapper;
import faang.school.analytics.mapper.RecommendationReceivedEventMapperImpl;
import faang.school.analytics.model.EventType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RecommendationReceivedEventListenerTest {

    @InjectMocks
    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Spy
    private RecommendationReceivedEventMapper recommendationReceivedEventMapper = new RecommendationReceivedEventMapperImpl();

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Captor
    ArgumentCaptor<AnalyticsEventDto> analyticsEventDtoCaptor;


    private final Long eventId = 1L;
    private final Long eventReceiverId = 2L;
    private final String validEventJson = String.format(
            "{\"id\":%s,\"authorId\":1,\"receiverId\":%s,\"createdAt\":[2025,8,8,12,3,35,211455000]}",
            eventId,
            eventReceiverId
    );
    private final String incompleteEventJson = "{\"id\":1,\"authorId\":1,\"createdAt\":[2025,8,8,12,3,35,211455000]}";

    @BeforeEach
     void beforeEach() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void listenParsesEventDtoAndSavesIfValidJson () throws Exception {
        recommendationReceivedEventListener.listen(validEventJson);

        verify(objectMapper).readValue(validEventJson, RecommendationReceivedEventDto.class);
        verify(analyticsEventService).saveEvent(analyticsEventDtoCaptor.capture());
        AnalyticsEventDto analyticsEventDto = analyticsEventDtoCaptor.getValue();
        assertEquals(eventReceiverId, analyticsEventDto.receiverId());
        assertEquals(EventType.RECOMMENDATION_RECEIVED, analyticsEventDto.eventType());
    }

    @Test
    void listenThrowsIfIncompleteEventData () throws Exception {
        assertThrows(EventDeserializationException.class, () -> recommendationReceivedEventListener.listen(incompleteEventJson));
        verifyNoInteractions(analyticsEventService);
    }

    @Test
    void listenThrowsIfInvalidJson () throws Exception {
        String invalidJson = "breaking text {}" + validEventJson;
        assertThrows(EventDeserializationException.class, () -> recommendationReceivedEventListener.listen(invalidJson));
        verifyNoInteractions(analyticsEventService);
    }
}
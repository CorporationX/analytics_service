package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventMapperTest {
    @Spy
    private AnalyticsEventMapperImpl mapper;
    private AnalyticsEvent analyticsEventExpected;
    private RecommendationEventDto recommendationEventExpected;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        analyticsEventExpected = AnalyticsEvent.builder()
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(dateTime)
                .build();
        recommendationEventExpected = RecommendationEventDto.builder()
                .authorId(1L)
                .recipientId(2L)
                .date(dateTime)
                .build();
    }

    @Test
    void toEntity_shouldMatchAllFields() {
        AnalyticsEvent actual = mapper.toEntity(recommendationEventExpected);
        assertEquals(analyticsEventExpected, actual);
    }

    @Test
    void toDto_shouldMatchAllFields() {
        RecommendationEventDto actual = mapper.toDto(analyticsEventExpected);
        assertEquals(recommendationEventExpected, actual);
    }

    @Test
    void getRecommendationType_shouldReturnRecommendationReceivedType() {
        assertEquals(EventType.RECOMMENDATION_RECEIVED, mapper.getRecommendationType());
    }
}
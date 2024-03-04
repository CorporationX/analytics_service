package faang.school.analytics.mapper;

import static org.junit.jupiter.api.Assertions.*;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    private AnalyticsEvent analyticsEvent;
    private RecommendationEvent recommendationEvent;

    @BeforeEach
    void setUp() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 22, 20, 6, 30);
        analyticsEvent = AnalyticsEvent.builder()
                .id(2L)
                .actorId(1L)
                .receiverId(3L)
                .receivedAt(fixedTime)
                .build();
        recommendationEvent = RecommendationEvent.builder()
                .authorId(1L)
                .recommendationId(2L)
                .receiverId(3L)
                .createdAt(fixedTime)
                .build();
    }

    @Test
    public void testMapper() {
        assertEquals(analyticsEvent, analyticsEventMapper.toEntity(recommendationEvent));
    }



}

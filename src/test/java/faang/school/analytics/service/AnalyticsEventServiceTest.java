package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Spy
    private AnalyticsEventMapperImpl mapper;

    @InjectMocks
    private AnalyticsEventService service;

    @Test
    void savePostEvent_ShouldMapCorrectly() {
        AnalyticsEvent actual = mapper.toAnalyticsEvent(mockPostViewEvent());

        Assertions.assertEquals(mockAnalyticsEvent(), actual);
    }

    @Test
    void savePostEvent_ShouldBeSaved() {
        service.savePostEvent(mockPostViewEvent());

        Mockito.verify(repository).save(Mockito.any(AnalyticsEvent.class));
    }

    private PostViewEvent mockPostViewEvent() {
        return new PostViewEvent(1L, 1L, null, 1L);
    }

    private AnalyticsEvent mockAnalyticsEvent() {
        return AnalyticsEvent.builder()
                .id(0L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_VIEW)
                .build();
    }
}

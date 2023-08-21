package faang.school.analytics.service;

<<<<<<< HEAD
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
=======
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
>>>>>>> griffon-master
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventService analyticsEventService;
    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private PostViewEventMapper postViewEventMapper;
    private AnalyticsEventMapper analyticsEventMapper;
    private PostViewEventDto eventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = new AnalyticsEventMapperImpl();
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository,analyticsEventMapper);
        eventDto = PostViewEventDto.builder()
                .authorId(1L)
                .postId(1L)
                .userId(1L)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(1L)
                .receiverId(1L)
                .eventType(EventType.POST_VIEW)
                .build();
    }

    @Test
    void saveEvent() {
        EventDto eventDto = new EventDto();
        analyticsEventService.saveEvent(eventDto);
        verify(analyticsEventRepository).save(analyticsEventMapper.toModel(eventDto));
    }

    @Test
    void testSaveEvent() {
        when(postViewEventMapper.toModel(eventDto)).thenReturn(analyticsEvent);
        analyticsEventService.saveEvent(eventDto);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
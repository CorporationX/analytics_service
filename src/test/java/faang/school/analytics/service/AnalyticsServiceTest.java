//package faang.school.analytics.service;
//
//import faang.school.analytics.dto.PostViewEventDto;
//import faang.school.analytics.mapper.PostViewMapper;
//import faang.school.analytics.model.AnalyticsEvent;
//import faang.school.analytics.model.EventType;
//import faang.school.analytics.repository.AnalyticsEventRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.time.LocalDateTime;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AnalyticsServiceTest {
//    @Mock
//    private AnalyticsEventRepository analyticsEventRepository;
//    @Spy
//    private PostViewMapper postViewMapper = Mappers.getMapper(PostViewMapper.class);
//    @InjectMocks
//    private AnalyticsService analyticsService;
//
//    @Test
//    void savePostEventTest(){
//        PostViewEventDto eventDto = PostViewEventDto.builder()
//                .postId(1L)
//                .viewerId(2L)
//                .viewDate(LocalDateTime.now())
//                .build();
//
//        AnalyticsEvent analyticsEvent = postViewMapper.ToAnalyticsEvent(eventDto);
//        analyticsEvent.setEventType(EventType.POST_VIEW);
//
//        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(analyticsEvent);
//
//        AnalyticsEvent result = analyticsService.savePostViewEvent(eventDto);
//
//        assertNotNull(result);
//        assertEquals(EventType.POST_VIEW, result.getEventType());
//    }
//}

package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void testSaveLikeAnalytics() throws IOException {
        Message message = mock(Message.class);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        LikeEvent likeEvent = new LikeEvent();

        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEvent);
        when(analyticsEventMapper.toAnalyticsEventFromLikeEvent(likeEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveLikeAnalytics(message);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}
package faang.school.analytics.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.mapper.FollowerEventMapper;
import faang.school.analytics.service.FollowerEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @Mock
    private FollowerEventService followerEventService;
    @Mock
    private FollowerEventMapper followerEventMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    void onMessage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto(1L, 101L,
                202L, "FOLLOWER", localDateTime);

        Message message = new Message() {
            @Override
            public byte[] getBody() {
                return new byte[100];
            }

            @Override
            public byte[] getChannel() {
                return new byte[100];
            }
        };

        byte[] pattern = new byte[100];
        when(followerEventMapper.toAnalyticsEventDto(any())).thenReturn(analyticsEventDto);
        followerEventListener.onMessage(message, pattern);
        verify(followerEventMapper, times(1)).toAnalyticsEventDto(any());
        verify(followerEventService, times(1)).save(any());
    }
}
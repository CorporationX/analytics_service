package faang.school.analytics.service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

public class FollowEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;


    @Mock
    private Message message;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    public void testOnMessage_success() throws IOException {

        byte[] pattern = new byte[]{};

        when( objectMapper.readValue( message.getBody(), FollowerEvent.class ) ).thenReturn( new FollowerEvent() );
        when( analyticsEventMapper.toEntity( new FollowerEvent() ) ).thenReturn( new AnalyticsEvent() );

        followerEventListener.onMessage( message, pattern );
        verify( analyticsEventService ).saveEventToDb( Mockito.any( AnalyticsEvent.class ) );

    }

    @Test
    public void testOnMessage_failure() throws IOException {

        byte[] pattern = new byte[]{};
        when( objectMapper.readValue( message.getBody(), FollowerEvent.class ) ).thenThrow( IOException.class );
        assertThrows( RuntimeException.class, () -> followerEventListener.onMessage( message, pattern ) );

    }
}







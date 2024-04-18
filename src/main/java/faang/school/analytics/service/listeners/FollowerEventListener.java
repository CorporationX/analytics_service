package faang.school.analytics.service.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent followerEvent;
        try {

            followerEvent = objectMapper.readValue( message.getBody(), FollowerEvent.class );

        } catch (IOException e) {
            log.warn( "Unsuccessful mapping", e );
            throw new RuntimeException( e );
        }

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity( followerEvent );
        analyticsEventService.saveEventToDb( analyticsEvent );
        log.info( "Data successfully passed to analyticsEventService" );

    }
}


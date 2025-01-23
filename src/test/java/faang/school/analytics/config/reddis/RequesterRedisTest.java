package faang.school.analytics.config.reddis;

import faang.school.analytics.config.reddis.impl.FollowerEventRequester;
import faang.school.analytics.listener.mentorshipoffered.FollowerEventListener;
import faang.school.analytics.model.mapper.FollowerEventMapper;
import faang.school.analytics.service.FollowerEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequesterRedisTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private FollowerEventService followerEventService;
    @Mock
    private FollowerEventMapper mapper;
    @InjectMocks
    private FollowerEventListener followerEventListener;

    private RequesterRedis requesterRedis;

    private String channelName;
    private ChannelTopic channelTopic;

    @BeforeEach
    void setUp() {
        channelName = "Test Channel";
        requesterRedis = new FollowerEventRequester(followerEventListener);
        ReflectionTestUtils.setField(requesterRedis, "channel", channelName);
        channelTopic = new ChannelTopic(channelName);

    }

    @Test
    void getRequesterSuccessTest() {

        Pair<MessageListenerAdapter, ChannelTopic> pair = requesterRedis.getRequester();

        assertEquals(followerEventListener, pair.getFirst().getDelegate());
        assertEquals(channelTopic, pair.getSecond());
    }

    @Test
    void getMethodListenerSuccessTest() {
        assertEquals(followerEventListener, requesterRedis.getMethodListener());
    }

    @Test
    void getChannelSuccessTest() {
        assertEquals(channelName, requesterRedis.getChannel());
    }
}
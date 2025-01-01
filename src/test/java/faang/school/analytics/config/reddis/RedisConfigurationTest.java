package faang.school.analytics.config.reddis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.mentorshipoffered.FollowerEventListener;
import faang.school.analytics.model.mapper.FollowerEventMapper;
import faang.school.analytics.service.FollowerEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisConfigurationTest {

    private RedisConfiguration redisConfiguration;
    @Mock
    private List<RequesterRedis<?>> requesters;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RequesterRedis<?> requesterRedis;
    @Mock
    private FollowerEventService followerEventService;
    @Mock
    private FollowerEventMapper mapper;
    @InjectMocks
    private FollowerEventListener followerEventListener;

    @BeforeEach
    void setUp() {
        requesters = Collections.singletonList(requesterRedis);
        redisConfiguration = new RedisConfiguration(objectMapper, requesters);
        ReflectionTestUtils.setField(redisConfiguration, "host", "localhost");
        ReflectionTestUtils.setField(redisConfiguration, "port", 49999);
    }

    @Test
    void jedisConnectionFactory() {
        JedisConnectionFactory factory = redisConfiguration.jedisConnectionFactory();
        assertNotNull(factory, "JedisConnectionFactory is null");
        assertTrue(factory.getStandaloneConfiguration() instanceof RedisStandaloneConfiguration,
                "getStandaloneConfiguration() instanceof RedisStandaloneConfiguration");
    }

    @Test
    void redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = redisConfiguration.redisTemplate();

        assertNotNull(redisTemplate, "RedisTemplate is null");
        assertNotNull(redisTemplate.getConnectionFactory(), "ConnectionFactory is null");
        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisSerializer,
                "getKeySerializer() instanceof StringRedisSerializer");
        assertNotNull(redisTemplate.getValueSerializer(), "ValueSerializer is null");
    }

    @Test
    void redisContainer() {
        JedisConnectionFactory factory = mock(JedisConnectionFactory.class);
        when(requesterRedis.getRequester()).thenReturn(Pair.of(
                new MessageListenerAdapter(followerEventListener),
                new ChannelTopic("testChannel")));

        RedisMessageListenerContainer container = redisConfiguration.redisContainer(factory);

        assertNotNull(container, "RedisMessageListenerContainer is null");
        assertEquals(factory, container.getConnectionFactory(),
                "ConnectionFactory is not matching a factory");

        verify(requesterRedis, times(2)).getRequester(); /// it should be number of implementation
    }
}
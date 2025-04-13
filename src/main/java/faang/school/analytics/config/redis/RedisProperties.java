package faang.school.analytics.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisProperties {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.connect_timeout}")
    private int connectTimeout;

    @Value("${spring.data.redis.read_timeout}")
    private int readTimeout;
}
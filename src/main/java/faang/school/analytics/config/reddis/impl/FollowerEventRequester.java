package faang.school.analytics.config.reddis.impl;

import faang.school.analytics.config.reddis.RequesterRedis;
import faang.school.analytics.listener.mentorshipoffered.FollowerEventListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
    public class FollowerEventRequester implements RequesterRedis <FollowerEventListener>{
        private final FollowerEventListener methodListener;

        @Value("${spring.data.redis.channels.followerChannel.name}")
        private String channel;
    }
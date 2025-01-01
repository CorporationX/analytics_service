package faang.school.analytics.config.reddis.impl;

import faang.school.analytics.config.reddis.RequesterRedis;
import faang.school.analytics.listener.mentorshipoffered.ProfileViewEventListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class ProfileViewEventRequester implements RequesterRedis<ProfileViewEventListener> {
    private final ProfileViewEventListener methodListener;

    @Value("${spring.data.redis.channels.profileViewChannel.name}")
    private String channel;
}
package faang.school.analytics.config.listeners;

import faang.school.analytics.listener.MentorshipRequestedEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipConfig {
    @Value("${spring.data.redis.channel.mentorship-requested}")
    private String mentorshipRequestedChannel;

    @Bean
    MessageListenerAdapter mentorshipRequestedListener(MentorshipRequestedEventListener mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> mentorshipRequested(
            @Qualifier("mentorshipRequestedListener") MessageListenerAdapter mentorshipRequestedListener) {
        return Pair.of(mentorshipRequestedListener, mentorshipRequestedTopic());
    }

    ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }
}
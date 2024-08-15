package faang.school.analytics.config.redis.project;

import faang.school.analytics.messaging.listener.project.ProjectViewEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class ProjectConfig {

    @Value("${spring.data.redis.channel.project-view}")
    private String projectViewChannelName;

    @Bean
    MessageListenerAdapter projectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> projectViewed(
            @Qualifier("projectViewListener") MessageListenerAdapter projectViewListener) {
        return Pair.of(projectViewListener, new ChannelTopic(projectViewChannelName));
    }
}

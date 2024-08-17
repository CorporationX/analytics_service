package faang.school.analytics.config.context;


import faang.school.analytics.listener.ProjectMessageConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectViewEventConfig {

    @Value("${spring.data.redis.channel.project-view}")
    private String redisChannelProjectView;

    public ChannelTopic topic() {
        return new ChannelTopic(redisChannelProjectView);
    }

    @Bean
    MessageListenerAdapter projectEventListener(ProjectMessageConsumer projectMessageConsumer) {
        return new MessageListenerAdapter(projectMessageConsumer);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> projectRequester(MessageListenerAdapter projectEventListener) {
        return Pair.of(projectEventListener, topic());
    }
}

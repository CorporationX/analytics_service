package faang.school.analytics.messaging;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FundRaisedEventListener implements MessageListener {

    private final JsonMapper jsonMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FundRaisedEvent event = getEvent(message);
    }

    private FundRaisedEvent getEvent(Message message) {
        return jsonMapper
                .toObject(Arrays.toString(message.getBody()), FundRaisedEvent.class)
                .orElseThrow(() -> new IllegalArgumentException("Can not deserialize FundRaisedEvent"));
    }
}

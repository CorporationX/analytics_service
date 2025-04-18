package faang.school.analytics.listener.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import org.springframework.stereotype.Component;

@Component
public class TestEventListener extends AbstractEventListener<String> {

    public TestEventListener(ObjectMapper objectMapper) {
        super(objectMapper, String.class);

    }

    @Override
    protected void handleEvent(String event) {
    }
}
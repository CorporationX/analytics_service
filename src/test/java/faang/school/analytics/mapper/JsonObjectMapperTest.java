package faang.school.analytics.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class JsonObjectMapperTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonObjectMapper jsonMapper;

    @Test
    void testFromJson_SuccessfulDeserialization() throws IOException {
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        byte[] jsonBytes = jsonString.getBytes();
        User user = new User("John", 30);

        when(objectMapper.readValue(jsonBytes, User.class)).thenReturn(user);

        User result = jsonMapper.fromJson(jsonBytes, User.class);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getAge(), result.getAge());

        verify(objectMapper).readValue(jsonBytes, User.class);
    }

    @Test
    void testFromJson_DeserializationError_ReturnsNull() throws IOException {
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        byte[] jsonBytes = jsonString.getBytes();

        when(objectMapper.readValue(jsonBytes, User.class)).thenThrow(new IOException("Deserialization error"));

        User result = jsonMapper.fromJson(jsonBytes, User.class);

        assertNull(result);
        verify(objectMapper).readValue(jsonBytes, User.class);
    }

    @Test
    void testFromJson_JsonProcessingException_ReturnsNull() throws IOException {
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        byte[] jsonBytes = jsonString.getBytes();

        when(objectMapper.readValue(jsonBytes, User.class)).thenThrow(new JsonProcessingException("Processing error") {});

        User result = jsonMapper.fromJson(jsonBytes, User.class);

        assertNull(result);
        verify(objectMapper).readValue(jsonBytes, User.class);
    }

    private static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
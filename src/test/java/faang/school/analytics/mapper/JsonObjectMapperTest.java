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
        byte[] jsonBytes = "JSON_BYTES".getBytes();
        Object expectedObject = new Object();

        when(objectMapper.readValue(jsonBytes, Object.class)).thenReturn(expectedObject);

        Object resultObject = jsonMapper.fromJson(jsonBytes, Object.class);

        verify(objectMapper).readValue(jsonBytes, Object.class);
        assertEquals(expectedObject, resultObject);
    }

    @Test
    void testFromJson_FailedDeserialization() throws IOException {
        byte[] jsonBytes = "JSON_BYTES".getBytes();

        when(objectMapper.readValue(jsonBytes, Object.class)).thenThrow(JsonProcessingException.class);

        Object resultObject = jsonMapper.fromJson(jsonBytes, Object.class);

        verify(objectMapper).readValue(jsonBytes, Object.class);
        assertNull(resultObject);
    }

}
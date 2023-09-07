package faang.school.analytics.messaging;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.service.FundRaisedEventHandler;
import faang.school.analytics.util.JsonMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FundRaisedEventListenerTest {

    @Mock
    JsonMapper mapper;
    @Mock
    FundRaisedEventHandler handler;
    @InjectMocks
    FundRaisedEventListener listener;

    @ParameterizedTest
    @MethodSource("getJson")
    @DisplayName("Can not deserialize FundRaisedEvent")
    void canNotDeserializeFundRaisedEvent(String json) {
        when(mapper.toObject(Arrays.toString(json.getBytes()), FundRaisedEvent.class))
                .thenReturn(Optional.ofNullable(null));

        Message message = new DefaultMessage("channel".getBytes(), json.getBytes());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> listener.onMessage(message, null));
        assertEquals("Can not deserialize FundRaisedEvent", exception.getMessage());
    }

    @Test
    @DisplayName("Successfully receive FundRaisedEvent")
    void successfullyReceiveFundRaisedEvent() {
        FundRaisedEvent event = FundRaisedEvent.builder()
                .donorId(123)
                .projectId(67890)
                .amount("100.00")
                .timestamp(LocalDateTime.parse("2023-09-07T12:34:56"))
                .build();
        String json = """
                {
                  "donorId": 123,
                  "projectId": 67890,
                  "amount": "100.00",
                  "timestamp": "2023-09-07T12:34:56"
                }
                """;

        when(mapper.toObject(Arrays.toString(json.getBytes()), FundRaisedEvent.class))
                .thenReturn(Optional.of(event));

        listener.onMessage(new DefaultMessage("channel".getBytes(), json.getBytes()), null);

        verify(handler, times(1)).save(event);
    }

    private static Stream<Arguments> getJson() {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                  "donorId": ,
                                  "projectId": 67890,
                                  "amount": "100.00",
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": ,
                                  "amount": "100.00",
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": ,
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": "",
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": "100.00",
                                  "timestamp":
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": 100,
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": "100.00",
                                  "timestamp": "2023-09-07"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": 123,
                                  "projectId": 67890,
                                  "amount": "100.00",
                                  "timestamp": "T12:34:56"
                                }
                                """
                ),
                Arguments.of(
                        """
                                {
                                  "donorId": ,
                                  "projectId": 67890,
                                  "timestamp": "2023-09-07T12:34:56"
                                }
                                """
                )
        );
    }
}
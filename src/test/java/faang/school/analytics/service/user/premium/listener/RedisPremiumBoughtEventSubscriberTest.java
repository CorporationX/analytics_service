package faang.school.analytics.service.user.premium.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static faang.school.analytics.util.AnalyticFabric.buildAnalyticsEvent;
import static faang.school.analytics.util.AnalyticFabric.buildAnalyticsEvents;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisPremiumBoughtEventSubscriberTest {
    private static final String JSON = "[{\"receiverId\":1,\"actorId\":2,\"receivedAt\":\"2024-10-12T14:20:19\"}]";
    private static final long USER_ID = 1L;
    private static final double COST = 10.0;
    private static final int DAYS = 31;
    private static final LocalDateTime PURCHASE_DATE = LocalDateTime.of(2000, 1, 1, 1, 1);

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private RedisPremiumBoughtEventSubscriber redisPremiumBoughtEventSubscriber;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Received message of redis topic successful")
    void testOnMessage() throws IOException {
        byte[] messageBody = JSON.getBytes();

        List<PremiumBoughtEventDto> premiumBoughtEventDtos = List.of(new PremiumBoughtEventDto(USER_ID, COST, DAYS, PURCHASE_DATE));
        List<AnalyticsEvent> newAnalyticsEvents = List.of(buildAnalyticsEvent(1L));

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(any(byte[].class), any(TypeReference.class))).thenReturn(premiumBoughtEventDtos);
        when(analyticsEventMapper.toAnalyticsEvents(premiumBoughtEventDtos)).thenReturn(newAnalyticsEvents);

        redisPremiumBoughtEventSubscriber.onMessage(message, null);

        verify(analyticsEventMapper).toAnalyticsEvents(premiumBoughtEventDtos);

        List<AnalyticsEvent> analyticsEvents = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisPremiumBoughtEventSubscriber, "analyticsEvents");
        assertThat(analyticsEvents).isNotNull();
        assertThat(analyticsEvents.size()).isEqualTo(1);
        assertThat(analyticsEvents.get(0)).isEqualTo(newAnalyticsEvents.get(0));
    }

    @Test
    @DisplayName("Test analytic events list is empty successful")
    void testAnalyticsEventsListIsEmpty() {
        assertThat(redisPremiumBoughtEventSubscriber.premiumBoughtEventListIsEmpty()).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Given an exception when save list then catch and return copyList into main list")
    void testSaveAllUserViewEventsException() {
        List<AnalyticsEvent> events = new ArrayList<>(buildAnalyticsEvents(1L, 3L));
        ReflectionTestUtils.setField(redisPremiumBoughtEventSubscriber, "analyticsEvents", events);
        when(analyticsEventService.saveAllEvents(anyList())).thenThrow(new RuntimeException());

        redisPremiumBoughtEventSubscriber.saveAllPremiumBoughtEvents();

        List<AnalyticsEvent> eventsListFromTestObject = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisPremiumBoughtEventSubscriber, "analyticsEvents");
        assertThat(eventsListFromTestObject).isNotEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Save all user view events successful")
    void testSaveAllUserViewEventsSuccessful() {
        List<AnalyticsEvent> events = new ArrayList<>(buildAnalyticsEvents(1L, 3L));
        ReflectionTestUtils.setField(redisPremiumBoughtEventSubscriber, "analyticsEvents", events);

        redisPremiumBoughtEventSubscriber.saveAllPremiumBoughtEvents();

        List<AnalyticsEvent> eventsListFromTestObject = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisPremiumBoughtEventSubscriber, "analyticsEvents");
        assertThat(eventsListFromTestObject).isEmpty();
    }
}
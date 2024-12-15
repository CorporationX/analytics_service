package faang.school.analytics.scheduler.analytic;

import faang.school.analytics.client.user.UserServiceClient;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.domain.dto.user.UpdateUsersRankDto;
import faang.school.analytics.mapper.user.UpdateUsersRankMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.service.events.AnalyticsEventService;
import faang.school.analytics.service.analytic.AverageValueOfActionCalculator;
import faang.school.analytics.service.analytic.StandardDeviationCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledRatingUpdaterTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AverageValueOfActionCalculator averageValueOfActionCalculator;

    @Spy
    private StandardDeviationCalculator standardDeviationCalculator;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private UpdateUsersRankMapper updateUsersRankMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private ScheduledRatingUpdater scheduledRatingUpdater;

    @Captor
    private ArgumentCaptor<List<AnalyticsEvent>> userActionsCountByUserIdCaptor;

    @Captor
    ArgumentCaptor<UpdateUsersRankDto> usersNewRanksByLastThreeHourActionsCaptor;

    @Captor
    ArgumentCaptor<Map<Long, Double>> usersRankById;

    @Test
    void updateUserRankScore_shouldCallUpdateUsersRankInBatch() {
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(Mockito.anyString()))
                .thenReturn(Optional.of(Arrays.asList(
                        AnalyticsEvent.builder().actorId(1L).build(),
                        AnalyticsEvent.builder().actorId(1L).build())));
        when(analyticsEventService.mapAnalyticEventsToActorActionsCount(userActionsCountByUserIdCaptor.capture()))
                .thenReturn(Map.of(1L, 2));
        when(analyticsEventService.getSumOfUsersActionsByEventType(List.of(2)))
                .thenReturn(2);
        when(updateUsersRankMapper.mapUsersRankByIdToUpdateUsersRankDto(usersRankById.capture()))
                .thenReturn(UpdateUsersRankDto.builder()
                        .usersRankByIds(Map.of(1L, 2.0))
                        .build());

        scheduledRatingUpdater.updateUserRankScore();

        verify(userServiceClient, times(1))
                .updateUsersRankByUserIds(usersNewRanksByLastThreeHourActionsCaptor.capture());
        assertNotNull(usersNewRanksByLastThreeHourActionsCaptor.getValue().getUsersRankByIds());
        assertEquals(Set.of(1L), usersNewRanksByLastThreeHourActionsCaptor.getValue().getUsersRankByIds().keySet());
        assertEquals(1, usersNewRanksByLastThreeHourActionsCaptor.getValue().getUsersRankByIds().keySet().size());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, null, 3, PROFILE_VIEW",
            "null, 5.0, 9, FOLLOWER",
            "7.0, null, 15, POST_VIEW"
    }, nullValues = {"null"})
    void calculateZScore_ReturnZero(Double avg, Double std, Integer usersActionCount, String currentEventType) {
        double res = scheduledRatingUpdater.calculateZScore(avg, std, usersActionCount, currentEventType);

        assertEquals(0.0, res);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1.0, 5.0, 1, PROFILE_VIEW",
            "25.0, 9.0, 25, FOLLOWER",
            "7.0, 15.0, 7, POST_VIEW"
    }, nullValues = {"null"})
    void calculateZScore_ReturnDefaultValue(Double avg, Double std, Integer usersActionCount, String currentEventType) {
        double res = scheduledRatingUpdater.calculateZScore(avg, std, usersActionCount, currentEventType);

        assertEquals(EventType.getWeightByName(currentEventType), res);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1.0, 5.0, 2, PROFILE_VIEW",
            "25.0, 9.0, 30, FOLLOWER",
            "7.0, 15.0, 1, POST_VIEW"
    }, nullValues = {"null"})
    void calculateZScore_DontReturnZeroOrDefaultValue(Double avg, Double std, Integer usersActionCount, String currentEventType) {
        double res = scheduledRatingUpdater.calculateZScore(avg, std, usersActionCount, currentEventType);

        assertNotEquals(0.0, res);
        assertNotEquals(EventType.getWeightByName(currentEventType), res);
    }
}
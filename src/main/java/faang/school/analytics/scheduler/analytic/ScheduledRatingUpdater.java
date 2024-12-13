package faang.school.analytics.scheduler.analytic;

import faang.school.analytics.client.user.UserServiceClient;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.mapper.user.UpdateUsersRankMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.service.events.AnalyticsEventService;
import faang.school.analytics.service.analytic.AverageValueOfActionCalculator;
import faang.school.analytics.service.analytic.StandardDeviationCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRatingUpdater {
    @Value("${rating.growth-intensive}")
    private double ratingGrowthIntensive;

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AverageValueOfActionCalculator averageValueOfActionCalculator;
    private final StandardDeviationCalculator standardDeviationCalculator;
    private final UserServiceClient userServiceClient;
    private final AnalyticsEventService analyticsEventService;
    private final UpdateUsersRankMapper updateUsersRankMapper;
    private final UserContext userContext;

    @Async
    @Scheduled(cron = "0 0 */3 * * *", zone = "Europe/Moscow")
    public void updateUserRankScore() {
        Map<Long, Double> usersRankById = new HashMap<>();
        log.info("updating users rank is starting");
        for (EventType eventType : EventType.values()) {
            String currentEventType = eventType.name();

            Map<Long, Integer> userActionsCountByUserId = analyticsEventService.mapAnalyticEventsToActorActionsCount(
                    analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(currentEventType)
                            .orElseGet(ArrayList::new)
            );

            int totalActionsSum = analyticsEventService
                    .getSumOfUsersActionsByEventType(new ArrayList<>(userActionsCountByUserId.values()));
            int totalUsersActionCount = userActionsCountByUserId.size();
            log.info("total actions sum: {}, by event type: {}, on active users count: {}",
                    totalActionsSum, currentEventType, totalUsersActionCount);

            Double avgAction = averageValueOfActionCalculator.calculate(totalActionsSum, totalUsersActionCount);
            Double stdDev = standardDeviationCalculator
                    .calculate(new ArrayList<>(userActionsCountByUserId.values()), avgAction, totalUsersActionCount);
            log.info("average value {}, standard deviation {} of action: {}",
                    avgAction, stdDev, currentEventType);

            userActionsCountByUserId.forEach((userId, actionCount) -> {
                double rankIncrement = calculateZScore(avgAction, stdDev, actionCount, currentEventType)
                        * ratingGrowthIntensive
                        * EventType.getWeightByName(currentEventType);
                usersRankById.merge(userId, rankIncrement, Double::sum);
                log.info("action count: {}, event type weight: {}, rating: {}",
                        actionCount, EventType.getWeightByName(currentEventType), usersRankById.get(userId));
            });
        }
        userContext.setUserId(1L);
        userServiceClient
                .updateUsersRankByUserIds(updateUsersRankMapper.mapUsersRankByIdToUpdateUsersRankDto(usersRankById));
    }

    public double calculateZScore(Double averageValueOfAction, Double standardDeviationOfAction, Integer userActionsCount, String currentEventType) {
        if (averageValueOfAction == null || standardDeviationOfAction == null) {
            return 0.0;
        }
        double resultOfMinusUserActionCountAndAverageValueOfAction = userActionsCount - averageValueOfAction;
        if (resultOfMinusUserActionCountAndAverageValueOfAction == 0.0) {
            return EventType.getWeightByName(currentEventType);
        }
        return (((userActionsCount - averageValueOfAction) / standardDeviationOfAction) * 100) / 100;
    }
}

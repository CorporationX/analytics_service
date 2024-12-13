package faang.school.analytics.mapper.user;

import faang.school.analytics.dto.user.UpdateUsersRankDto;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateUsersRankMapper {

    @Value("${rating.half-max-rank}")
    private Double halfUserRank;

    @Value("${rating.max-user-rating}")
    private Double maximumUserRating;

    @Value("${rating.min-user-rating}")
    private Double minimumUserRating;

    @Value("${rating.growth-intensive}")
    private Double ratingGrowthIntensive;

    public UpdateUsersRankDto mapUsersRankByIdToUpdateUsersRankDto(Map<Long, Double> usersRankById) {
        return UpdateUsersRankDto.builder()
                .usersRankByIds(usersRankById)
                .halfUserRank(halfUserRank)
                .maximumUserRating(maximumUserRating)
                .minimumUserRating(minimumUserRating)
                .ratingGrowthIntensive(ratingGrowthIntensive)
                .maximumGrowthRating(EventType.getMaximumRating())
                .build();
    }
}

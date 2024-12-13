package faang.school.analytics.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UpdateUsersRankDto {

    @NotNull
    private Map<Long, Double> usersRankByIds;

    @NotNull
    private Double maximumGrowthRating;

    @NotNull
    private Double halfUserRank;

    @NotNull
    private Double maximumUserRating;

    @NotNull
    private Double minimumUserRating;

    @NotNull
    private Double ratingGrowthIntensive;
}

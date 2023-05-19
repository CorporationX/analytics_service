package faang.school.analytics.repository;

import faang.school.analytics.model.UserProfileView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface UserProfileViewRepository extends CrudRepository<UserProfileView, Long> {

    Stream<UserProfileView> findByUserId(long userId);
}

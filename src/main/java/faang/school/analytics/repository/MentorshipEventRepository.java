package faang.school.analytics.repository;

import faang.school.analytics.dto.MentorshipEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentorshipEventRepository extends JpaRepository<MentorshipEventDto, Long> {

    List<MentorshipEventDto> findBySenderIdOrReceiverIdAndTimestampBetween(
            long senderId,
            long receiverId,
            LocalDateTime from,
            LocalDateTime to);

}
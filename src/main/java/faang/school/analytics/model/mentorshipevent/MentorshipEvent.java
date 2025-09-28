package faang.school.analytics.model.mentorshipevent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="mentorship_event")
public class MentorshipEvent {
    public void setMenteeId(long menteeId) {
        long id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private long mentorId;
    private long menteeId;
    private LocalDateTime timeStamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMentorId() {
        return mentorId;
    }

    public long getMenteeId() {
        return menteeId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
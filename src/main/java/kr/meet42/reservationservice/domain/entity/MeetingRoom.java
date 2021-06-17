package kr.meet42.reservationservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean availability;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time time;

    @Column(nullable = false)
    private Long room_type;

    @Builder
    public MeetingRoom(String location, boolean availability, Date date, Time time, Long room_type) {
        this.location = location;
        this.availability = availability;
        this.date = date;
        this.time = time;
        this.room_type = room_type;
    }

    public MeetingRoom update(boolean availability) {
        this.availability = availability;

        return this;
    }
}
